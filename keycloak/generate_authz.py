#!/usr/bin/env python3
"""
Single source of truth for knowvera-api authorization. Run from the repository root:

    python3 keycloak/generate_authz.py

From ROLES, GRANTS, RESOURCES, PUBLIC_ENDPOINTS and the controllers of the webservice
(webservice/target/src/main/java: AndroMDA generated *Api interfaces, webservice/src/main/java:
hand written controllers) it regenerates:

  - keycloak/knowvera-api-authz.json                    Authorization tab import for knowvera-api
  - keycloak/ROLE_MATRIX.md                             role / resource / scope matrix for review
  - knowvera-realm.json                                 the realm export, generated parts updated in place
  - webservice/src/test/resources/test-realm.json      the same realm for tests: test secrets, seed users
  - webservice/src/main/resources/keycloak/policy-enforcer.json   policy enforcer paths
  - webservice/.../config/PublicEndpoints.java          the permitAll matchers of SpringSecurityConfig

It stops without writing anything when a rule is missing: an API not declared in RESOURCES
(or declared but gone), an endpoint whose action no rule gives (add it to ENDPOINT_SCOPES), or
an unknown role, resource or scope in GRANTS. New or renamed endpoints cannot ship unprotected.

Design:
  - One Keycloak resource per API, named after its base path; one scope per action (SCOPES),
    derived from the method name by classify().
  - GRANTS says which role may perform which actions on which resource. Owner-scoped roles
    (OWNER_SCOPED) receive "<scope>-own" instead of "<scope>" (except on reference data);
    @RequiresOwnership then requires them to own the record.
  - Permissions are scope permissions per (resource, scope), AFFIRMATIVE over role policies.
  - Spring sees each granted permission as the authority SCOPE_<resource>:<scope>
    (KeycloakPermissionConverter); keycloak/update_preauthorize.py writes the matching
    @PreAuthorize expressions into the UML model.
"""

import difflib
import json
import re
import sys
import uuid
from collections import defaultdict
from pathlib import Path

HERE = Path(__file__).resolve().parent
ROOT = HERE.parent
SOURCE_DIRS = [
    ROOT / "webservice/target/src/main/java",
    ROOT / "webservice/src/main/java",
]
REALM_FILE = ROOT / "knowvera-realm.json"
ENFORCER_FILE = ROOT / "webservice/src/main/resources/keycloak/policy-enforcer.json"
CLIENT_ID = "knowvera-api"
REALM = "knowvera"
AUTHENTICATED_ROLE = f"default-roles-{REALM}"
AUTHENTICATED_POLICY = "authenticated-policy"

PUBLIC_ENDPOINTS_FILE = ROOT / "webservice/src/main/java/bw/co/knowvera/config/PublicEndpoints.java"

# --------------------------------------------------------------------------
# Every API, by the resource name derived from its base path. The script stops on an API
# that is not declared here (or a declaration whose API is gone), so a new or renamed
# controller cannot ship without a decision on who may use it.
# --------------------------------------------------------------------------
RESOURCES = {
    "analytics":                   "Platform usage counts",
    "audit-logs":                  "Audit trail",
    "client-requests":             "Organisations' requests to onboard their clients",
    "contacts":                    "Contact details",
    "document-types":              "Reference: document types",
    "documents":                   "Uploaded documents and their verification",
    "employment-records":          "Individuals' employment history",
    "expected-fields":             "Reference: fields expected on a document type",
    "individuals":                 "Individuals (KYC subjects)",
    "invoices":                    "Invoices",
    "kyc-field-groups":            "Reference: KYC field groups",
    "kyc-records":                 "KYC cases",
    "kyc-report-sections":         "Sections of a KYC report",
    "novu":                        "Notification service configuration",
    "organisation-branches":       "Organisation branches",
    "organisation-document-types": "Documents an organisation requires",
    "organisations":               "Organisations (KYC subjects and clients)",
    "sequences":                   "Reference number sequences",
    "settings":                    "Platform settings",
    "subscriptions":               "Subscriptions",
    "users":                       "User accounts and their roles",
    "verification-data-configs":   "Reference: data verification configuration",
}

# Endpoints reachable without a token, by Class.method. They get no Keycloak permission and
# are written to PublicEndpoints.java, which SpringSecurityConfig permits.
PUBLIC_ENDPOINTS = {
    "AnalyticsApi.countAnalytics",
    "AnalyticsApi.organisationCountAnalytics",
    "ClientRequestApi.confirmToken",          # emailed confirmation link
    "ClientRequestApi.confirmRegistration",   # emailed registration link
    "IndividualApi.loadRequestIndividual",    # registration flow, guarded by its token
    "OrganisationApi.loadRequestOrganisation",
}
# Framework endpoints, all HTTP methods (Spring path patterns)
PUBLIC_INFRASTRUCTURE = ["/swagger-ui/**", "/v3/**", "/actuator/**"]

# Action for endpoints whose name does not follow the conventions in classify(), by Class.method
ENDPOINT_SCOPES = {}

# --------------------------------------------------------------------------
# Scopes: actions, ordered roughly by privilege
# --------------------------------------------------------------------------
SCOPES = {
    "self":   "Read or change the caller's own data (my records, my profile)",
    "view":   "Read a single record",
    "list":   "List, page or search records",
    "edit":   "Create or update a record (save is an upsert)",
    "delete": "Remove a record or one of its parts",
    "submit": "Start a KYC application or attach evidence to one",
    "verify": "Run automated checks: extraction, analysis, data verification, screening",
    "review": "Change the review/decision status of a case, document or request",
    "export": "Download files and generate reports",
    "import": "Bulk upload records or templates",
    "manage": "Grant or revoke user roles",
}

# Method name rules, first match wins. HTTP method rules follow in classify().
SCOPE_RULES = [
    ("self",   r"^(findMy|loadMe$|loadMyOrganisation$|getNovuConfig$|changePassword$)"),
    ("manage", r"^(add|remove)(Client)?Roles?$"),
    ("import", r"^upload(Requests|Template)$"),
    ("export", r"^(download|generateKycReport$)"),
    ("verify", r"^(runVerification$|verify|analyse|textExtr)"),
    ("review", r"^(updateStatus|updateVerificationStatus)$"),
    ("submit", r"^(createNew|createIndividualRecord|createOrganisationRecord|updateRecordFiles"
               r"|removeRecordFile|upload|updateFileContent)$"),
    ("edit",   r"^(generateInvoice|attachDocumentType)$"),  # state-changing GETs
]

# --------------------------------------------------------------------------
# Roles and grants
# --------------------------------------------------------------------------
ROLES = {
    "PLATFORM_ADMIN":     "Operates the platform: configuration, users, billing, everything",
    "ORG_ADMIN":          "Administers a client organisation: its users, branches, client requests and billing",
    "KYC_ANALYST":        "Collects and captures KYC data and evidence, runs automated checks",
    "KYC_REVIEWER":       "Reviews captured KYC cases and documents (first line review)",
    "KYC_APPROVER":       "Takes the approve / reject decision on reviewed KYC cases",
    "RISK_ANALYST":       "Assesses and records customer risk",
    "SCREENING_ANALYST":  "Screens subjects against sanctions, PEP and adverse media lists",
    "COMPLIANCE_OFFICER": "Oversees KYC compliance and owns the KYC requirements configuration",
    "MLRO":               "Money Laundering Reporting Officer: escalations and regulatory reporting",
    "CASE_MANAGER":       "Routes and tracks client requests and KYC cases through the workflow",
    "AUDITOR":            "Read-only access to everything, including audit logs",
    "APPLICANT":          "Individual or organisation applying for KYC: self service only",
}

ALL = tuple(SCOPES)
READ = ("view", "list")
READ_EXPORT = ("view", "list", "export")

REFERENCE = ["document-types", "expected-fields", "kyc-field-groups", "verification-data-configs"]
# Shared reference data: nobody owns it, so owner-scoped roles get the plain scopes there
REFERENCE_RESOURCES = set(REFERENCE) | {"settings"}

# Roles limited to their own records. Keycloak grants them "<scope>-own" instead of "<scope>"
# (except on REFERENCE_RESOURCES); @RequiresOwnership lets "<scope>" holders through and
# requires "<scope>-own" holders to own the record.
OWNER_SCOPED = ["APPLICANT", "ORG_ADMIN"]
OWN = "-own"
SUBJECTS = ["individuals", "organisations", "employment-records", "contacts"]
CASES = ["kyc-records", "kyc-report-sections", "documents", "client-requests"]


def grant(resources, scopes):
    return {r: tuple(scopes) for r in resources}


GRANTS = {
    "PLATFORM_ADMIN": {"*": ALL},

    "ORG_ADMIN": {
        "organisations": ("view", "list", "edit"),
        "organisation-branches": ("view", "list", "edit", "delete"),
        "organisation-document-types": ("view", "list", "edit", "delete"),
        "users": ("view", "list", "edit", "manage"),
        "client-requests": ("view", "list", "edit", "delete", "review", "import", "export"),
        "individuals": READ,
        "kyc-records": ("view", "list", "edit", "submit", "export"),
        "documents": ("view", "list", "edit", "delete", "submit", "export"),
        "subscriptions": READ,
        "invoices": ("view", "list", "submit", "export"),
        "contacts": ("view", "list", "edit"),
        "analytics": ("view",),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "KYC_ANALYST": {
        "kyc-records": ("view", "list", "edit", "submit", "verify", "export"),
        "kyc-report-sections": ("view", "edit"),
        "documents": ("view", "list", "edit", "submit", "verify", "export"),
        "individuals": ("view", "list", "edit", "verify"),
        "organisations": ("view", "list", "verify"),
        "employment-records": ("view", "list", "edit"),
        "contacts": ("view", "list", "edit"),
        "client-requests": READ,
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "KYC_REVIEWER": {
        "kyc-records": ("view", "list", "verify", "review", "export"),
        "kyc-report-sections": ("view", "edit"),
        "documents": ("view", "list", "verify", "review", "export"),
        "individuals": ("view", "list", "verify"),
        "organisations": ("view", "list", "verify"),
        **grant(["employment-records", "contacts", "client-requests"], READ),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "KYC_APPROVER": {
        "kyc-records": ("view", "list", "review", "export"),
        "kyc-report-sections": ("view",),
        "documents": ("view", "list", "review", "export"),
        **grant(SUBJECTS + ["client-requests"], READ),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "RISK_ANALYST": {
        "kyc-records": READ_EXPORT,
        "kyc-report-sections": ("view", "edit"),
        "documents": READ,
        **grant(SUBJECTS, READ),
        "analytics": ("view",),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "SCREENING_ANALYST": {
        "kyc-records": ("view", "list", "verify"),
        "kyc-report-sections": ("view", "edit"),
        "documents": ("view", "list", "verify"),
        "individuals": ("view", "list", "verify"),
        "organisations": ("view", "list", "verify"),
        **grant(["employment-records", "contacts"], READ),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "COMPLIANCE_OFFICER": {
        **grant(CASES + SUBJECTS, READ_EXPORT),
        "kyc-records": ("view", "list", "review", "export"),
        "documents": ("view", "list", "review", "export"),
        "client-requests": ("view", "list", "review", "export"),
        "kyc-report-sections": ("view", "edit"),
        **grant(REFERENCE, ("view", "list", "edit", "delete")),
        "organisation-document-types": ("view", "list", "edit"),
        "organisation-branches": READ,
        "audit-logs": READ,
        "users": READ,
        "analytics": ("view",),
        "settings": READ,
    },

    "CASE_MANAGER": {
        "client-requests": ("view", "list", "edit", "delete", "review", "import", "export"),
        "kyc-records": ("view", "list", "edit", "submit", "export"),
        "kyc-report-sections": ("view",),
        "documents": ("view", "list", "submit", "export"),
        "individuals": ("view", "list", "edit"),
        "organisations": ("view", "list", "edit"),
        "employment-records": ("view", "list", "edit"),
        "contacts": ("view", "list", "edit"),
        "organisation-branches": READ,
        "users": READ,
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "AUDITOR": {"*": READ_EXPORT},

    "APPLICANT": {
        "kyc-records": ("view", "edit", "submit"),
        "documents": ("view", "edit", "delete", "submit", "export"),
        "individuals": ("view", "edit"),
        "organisations": ("view", "edit"),
        "employment-records": ("view", "list", "edit", "delete"),
        "contacts": ("view", "edit"),
        "client-requests": ("view",),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },
}

# MLRO: everything the compliance officer has, plus re-running checks on cases and subjects
GRANTS["MLRO"] = {
    **GRANTS["COMPLIANCE_OFFICER"],
    "kyc-records": ("view", "list", "verify", "review", "export"),
    "documents": ("view", "list", "verify", "review", "export"),
    "individuals": ("view", "list", "verify", "export"),
    "organisations": ("view", "list", "verify", "export"),
}

# --------------------------------------------------------------------------
# Controller scanning
# --------------------------------------------------------------------------
CLASS_MAPPING = re.compile(r'^@RequestMapping\(\s*(?:value\s*=\s*)?"([^"]*)"', re.M)
ARGS = r'\((?:"[^"]*"|[^()"]|\([^()]*\))*\)'  # annotation arguments, quote aware
ENDPOINT = re.compile(
    r'@(Get|Post|Put|Delete|Patch)Mapping\s*(' + ARGS + r')?'  # mapping annotation
    r'((?:\s*@\w+(?:' + ARGS + r')?)*)'                       # other annotations
    r'\s*public\s+([^;{(]*?)\s*(\w+)\s*\(',                   # return type + name
    re.S,
)


def mapping_path(args):
    if not args:
        return ""
    m = re.search(r'(?:value|path)\s*=\s*\{?\s*"([^"]*)"', args) or re.search(r'^\(\s*"([^"]*)"', args)
    return m.group(1) if m else ""


def join_path(base, sub):
    return "/" + "/".join(p.strip("/") for p in (base, sub) if p.strip("/"))


EDIT_PREFIXES = ("save", "add", "attach", "update", "upload", "create", "generate", "set", "change")
READ_PREFIXES = ("find", "get", "load", "count", "organisationCount")


def classify(label, http, name, return_type):
    """The endpoint's action, or None when no rule applies (the script then stops)."""
    if label in PUBLIC_ENDPOINTS:
        return "public"
    if label in ENDPOINT_SCOPES:
        return ENDPOINT_SCOPES[label]
    for scope, rule in SCOPE_RULES:
        if re.search(rule, name):
            return scope
    if http == "DELETE" or name.startswith(("remove", "delete", "detach")):
        return "delete"
    if re.search(r"search", name, re.I) or name.startswith("getAll"):
        return "list"
    if http in ("POST", "PUT", "PATCH") and name.startswith(EDIT_PREFIXES):
        return "edit"
    if name.startswith(READ_PREFIXES):
        return "list" if re.search(r"\b(List|Page|Collection|Set)<", return_type) else "view"
    return None


def resource_name(base):
    return base.strip("/").replace("/", "-") or "root"


def display_name(cls):
    words = re.sub(r"(Api|Controller)$", "", cls)
    return re.sub(r"(?<=[a-z])(?=[A-Z])", " ", words)


def scan(errors):
    resources = {}
    found_public = set()
    for src in SOURCE_DIRS:
        for file in sorted(src.rglob("*.java")):
            text = file.read_text()
            cls = CLASS_MAPPING.search(text)
            if not cls:
                continue
            base = cls.group(1)
            if resource_name(base) not in RESOURCES:
                close = difflib.get_close_matches(resource_name(base), RESOURCES, n=1)
                errors.append(f"{file.stem}: API '{resource_name(base)}' is not declared in RESOURCES"
                              + (f" (renamed from '{close[0]}'?)" if close else ""))
            res = resources.setdefault(resource_name(base), {
                "displayName": display_name(file.stem),
                "endpoints": [],
            })
            for m in ENDPOINT.finditer(text):
                http, args, _, ret, method = m.groups()
                http = http.upper()
                label = f"{file.stem}.{method}"
                scope = classify(label, http, method, ret)
                if scope is None:
                    errors.append(f"{label}: no rule gives its action; add it to ENDPOINT_SCOPES")
                if scope == "public":
                    found_public.add(label)
                res["endpoints"].append({
                    "method": method,
                    "label": label,
                    "http": http,
                    "path": join_path(base, mapping_path(args)),
                    "scope": scope,
                })
    resources = {k: v for k, v in resources.items() if v["endpoints"]}
    errors += [f"RESOURCES declares '{r}' but no API has that base path" for r in RESOURCES if r not in resources]
    errors += [f"PUBLIC_ENDPOINTS lists unknown endpoint {e}" for e in sorted(PUBLIC_ENDPOINTS - found_public)]
    errors += [f"ENDPOINT_SCOPES lists unknown endpoint {e}" for e in ENDPOINT_SCOPES
               if not any(x["label"] == e for r in resources.values() for x in r["endpoints"])]
    return resources


def path_regex(pattern):
    parts = re.split(r"(\{[^}]+\}|\*)", pattern)
    rx = "".join("[^/]+" if p.startswith("{") else ".*" if p == "*" else re.escape(p) for p in parts)
    return re.compile("^" + rx + "$")


def scopes_of(resource):
    """Keycloak scopes the resource's endpoints need (public endpoints need none)."""
    return [s for s in SCOPES if any(e["scope"] == s for e in resource["endpoints"])]


# --------------------------------------------------------------------------
# Building
# --------------------------------------------------------------------------
def policy_name(role):
    return role.lower().replace("_", "-") + "-policy"


def resolve_grants(resources, errors):
    """Return {(resource, scope): set(roles)} for scopes that exist on the resource."""
    granted = defaultdict(set)
    available = {n: set(scopes_of(r)) for n, r in resources.items()}
    for role, per_resource in GRANTS.items():
        if role not in ROLES:
            errors.append(f"GRANTS references unknown role {role}")
        for res, scopes in per_resource.items():
            for s in scopes:
                if s not in SCOPES:
                    errors.append(f"{role}: unknown scope '{s}' on {res}")
            targets = available if res == "*" else {res: available.get(res)}
            for name, scopes_here in targets.items():
                if scopes_here is None:
                    close = difflib.get_close_matches(name, available, n=1)
                    hint = f" (renamed to '{close[0]}'?)" if close else ""
                    errors.append(f"{role}: unknown resource '{name}'{hint}")
                    continue
                for s in scopes:
                    if s in scopes_here:
                        granted[(name, granted_scope(role, name, s))].add(role)
    return granted


def granted_scope(role, resource, scope):
    if role in OWNER_SCOPED and resource not in REFERENCE_RESOURCES and scope != "self":
        return scope + OWN
    return scope


def resource_scopes(name, resource, granted):
    """Scopes on the Keycloak resource: those its endpoints use, plus granted "-own" variants."""
    scopes = []
    for s in scopes_of(resource):
        scopes.append(s)
        if granted.get((name, s + OWN)):
            scopes.append(s + OWN)
    return scopes


def scope_description(scope):
    base = scope[:-len(OWN)] if scope.endswith(OWN) else scope
    return SCOPES[base] + (" (own records only)" if scope.endswith(OWN) else "")


def build(resources, granted):
    policies = [{
        "name": AUTHENTICATED_POLICY,
        "description": "Any authenticated user of the realm",
        "type": "role",
        "logic": "POSITIVE",
        "decisionStrategy": "UNANIMOUS",
        "config": {"fetchRoles": "false", "roles": json.dumps([{"id": AUTHENTICATED_ROLE, "required": False}])},
    }]
    for role, description in ROLES.items():
        policies.append({
            "name": policy_name(role),
            "description": description,
            "type": "role",
            "logic": "POSITIVE",
            "decisionStrategy": "UNANIMOUS",
            "config": {"fetchRoles": "false", "roles": json.dumps([{"id": role, "required": False}])},
        })

    authz_resources, permissions = [], []
    for name in sorted(resources):
        res = resources[name]
        scopes = resource_scopes(name, res, granted)
        authz_resources.append({
            "name": name,
            "displayName": res["displayName"],
            "type": f"urn:{CLIENT_ID}:resources:{name}",
            "ownerManagedAccess": False,
            "attributes": {},
            "uris": sorted({e["path"] for e in res["endpoints"]}),
            "scopes": [{"name": s} for s in scopes],
        })
        for s in scopes:
            if s == "self":
                apply = [AUTHENTICATED_POLICY]
            else:
                apply = [policy_name(r) for r in ROLES if r in granted[(name, s)]]
            if not apply:
                continue
            permissions.append({
                "name": f"{name}-{s}-permission",
                "description": f"{scope_description(s)} ({res['displayName']})",
                "type": "scope",
                "logic": "POSITIVE",
                "decisionStrategy": "AFFIRMATIVE",
                "config": {
                    "resources": json.dumps([name]),
                    "scopes": json.dumps([s]),
                    "applyPolicies": json.dumps(apply),
                },
            })

    return {
        "allowRemoteResourceManagement": True,
        "policyEnforcementMode": "ENFORCING",
        "decisionStrategy": "UNANIMOUS",
        "resources": authz_resources,
        "policies": policies + permissions,
        "scopes": [{"name": s, "displayName": scope_description(s)}
                   for s in all_scopes(authz_resources)],
    }


def all_scopes(authz_resources):
    used = {sc["name"] for r in authz_resources for sc in r["scopes"]}
    ordered = [x for s in SCOPES for x in (s, s + OWN)]
    return [s for s in ordered if s in used or s in SCOPES]


def build_enforcer(resources, granted):
    by_path = defaultdict(lambda: {"name": None, "methods": defaultdict(set)})
    for name, res in resources.items():
        for e in res["endpoints"]:
            if e["scope"] == "public":
                continue
            entry = by_path[e["path"]]
            entry["name"] = name
            entry["methods"][e["http"]].add(e["scope"])
            if granted.get((name, e["scope"] + OWN)):
                # ownership is checked by Spring (@RequiresOwnership), Keycloak accepts either
                entry["methods"][e["http"]].add(e["scope"] + OWN)
    # Not authorized by Keycloak: public endpoints, framework endpoints and Spring Boot's
    # error page (forwarded to after failures)
    public = sorted({e["path"] for r in resources.values() for e in r["endpoints"] if e["scope"] == "public"})
    disabled = [p.replace("/**", "/*") for p in PUBLIC_INFRASTRUCTURE] + public + ["/error"]
    paths = [{"path": p, "enforcement-mode": "DISABLED"} for p in disabled]
    for path in sorted(by_path):
        if path in public:
            continue
        entry = by_path[path]
        paths.append({
            "name": entry["name"],
            "path": path,
            "methods": [{"method": m, "scopes": sorted(s), **({"scopes-enforcement-mode": "ANY"} if len(s) > 1 else {})}
                        for m, s in sorted(entry["methods"].items())],
        })
    return {"enforcement-mode": "ENFORCING", "paths": paths}


def build_matrix(resources, granted):
    abbrev = {
        "PLATFORM_ADMIN": "PA", "ORG_ADMIN": "OA", "KYC_ANALYST": "KAN", "KYC_REVIEWER": "KRV",
        "KYC_APPROVER": "KAP", "RISK_ANALYST": "RA", "SCREENING_ANALYST": "SA",
        "COMPLIANCE_OFFICER": "CO", "MLRO": "MLRO", "CASE_MANAGER": "CM", "AUDITOR": "AUD", "APPLICANT": "APP",
    }
    lines = [
        "# knowvera-api role matrix",
        "",
        "Generated by `keycloak/generate_authz.py`. Edit `GRANTS` / `SCOPE_RULES` there, not this file.",
        "",
        "## Roles",
        "",
        "| Role | Abbrev. | Description |",
        "|---|---|---|",
        *[f"| `{r}` | {abbrev[r]} | {d} |" for r, d in ROLES.items()],
        "",
        "## Scopes",
        "",
        "| Scope | Meaning |",
        "|---|---|",
        *[f"| `{s}` | {d} |" for s, d in SCOPES.items()],
        "",
        "Each granted (resource, scope) is available to Spring as the authority "
        "`SCOPE_<resource>:<scope>`, e.g. `@PreAuthorize(\"hasAuthority('SCOPE_organisations:view')\")`.",
        "",
        f"Owner-scoped roles ({', '.join(OWNER_SCOPED)}) are granted `<scope>{OWN}` instead of `<scope>` "
        "(shown as \"own:\" below), except on reference data. On endpoints annotated with "
        "`@RequiresOwnership` they must own the record; elsewhere a `-own` grant does not open the endpoint.",
        "",
        "`self` is granted to every authenticated user and is left out of the table below.",
        "",
        "## Resource × role",
        "",
        "| Resource | " + " | ".join(abbrev[r] for r in ROLES) + " |",
        "|---|" + "---|" * len(ROLES),
    ]
    for name in sorted(resources):
        scopes = [s for s in scopes_of(resources[name]) if s != "self"]
        if not scopes:
            lines.append(f"| `{name}` | " + " | ".join("self" for _ in ROLES) + " |")
            continue
        cells = []
        for role in ROLES:
            have = [s for s in scopes if role in granted[(name, s)]]
            own = [s for s in scopes if role in granted[(name, s + OWN)]]
            cell = "**all**" if have and have == scopes else ", ".join(have)
            if own:
                cell = (cell + "; " if cell else "") + "own: " + ", ".join(own)
            cells.append(cell or "–")
        lines.append(f"| `{name}` | " + " | ".join(cells) + " |")

    lines += ["", "## Endpoints", ""]
    order = ["public"] + list(SCOPES)
    for name in sorted(resources):
        lines += [f"### `{name}`", "", RESOURCES.get(name, ""), "",
                  "| Scope | Authority | Endpoint | Method |", "|---|---|---|---|"]
        for e in sorted(resources[name]["endpoints"], key=lambda e: (order.index(e["scope"]), e["path"])):
            flag = ""
            authority = "–" if e["scope"] in ("self", "public") else f"`SCOPE_{name}:{e['scope']}`"
            if granted.get((name, e["scope"] + OWN)):
                authority += f" / `SCOPE_{name}:{e['scope']}{OWN}`"
            lines.append(f"| `{e['scope']}` | {authority} | `{e['http']} {e['path']}`{flag} | `{e['method']}` |")
        lines.append("")
    return "\n".join(lines)


def write_public_endpoints(resources):
    endpoints = sorted({(e["http"], re.sub(r"\{[^}]+\}", "*", e["path"]))
                        for r in resources.values() for e in r["endpoints"] if e["scope"] == "public"})
    infrastructure = ",\n".join(f'            "{p}"' for p in PUBLIC_INFRASTRUCTURE)
    entries = ",\n".join(f'            new Endpoint(HttpMethod.{h}, "{p}")' for h, p in endpoints)
    PUBLIC_ENDPOINTS_FILE.write_text(f"""package bw.co.knowvera.config;

import java.util.List;

import org.springframework.http.HttpMethod;

/**
 * Endpoints reachable without a token.
 * <p>
 * Generated by {{@code keycloak/generate_authz.py}} from PUBLIC_ENDPOINTS and PUBLIC_INFRASTRUCTURE;
 * do not edit by hand.
 */
public final class PublicEndpoints {{

    public record Endpoint(HttpMethod method, String pattern) {{
    }}

    /** Framework endpoints (all HTTP methods). */
    public static final List<String> INFRASTRUCTURE = List.of(
{infrastructure});

    /** API endpoints. */
    public static final List<Endpoint> ENDPOINTS = List.of(
{entries});

    private PublicEndpoints() {{
    }}
}}
""")
    return endpoints


# --------------------------------------------------------------------------
# Realm. knowvera-realm.json is a full Keycloak export (client scopes, flows, SMTP, ...);
# the generator owns the parts below and leaves the rest as exported.
# --------------------------------------------------------------------------
TEST_REALM_FILE = ROOT / "webservice/src/test/resources/test-realm.json"
REGISTRATION_CLIENT = "knowvera-registration"
PORTAL_CLIENTS = ["admin-portal", "knowvera-web"]
# Resolved from the environment when Keycloak imports the realm at start-up (--import-realm)
CLIENT_SECRETS = {CLIENT_ID: "${API_CLIENT_SECRET}", REGISTRATION_CLIENT: "${REGISTRATION_CLIENT_SECRET}"}
# KeycloakService.createRegistrationKeycloak() uses this service account to create users, reset
# passwords, map roles and manage Keycloak Organizations (manage-realm: no narrower role exists).
REGISTRATION_SERVICE_ACCOUNT_ROLES = ["manage-users", "manage-realm"]
TEST_SECRET = "test-secret"
TEST_PASSWORD = "password"

# Stable ids, so re-imports update rather than duplicate
ID_NAMESPACE = uuid.UUID("5f0c3a52-4f0e-4c41-9b0f-6b1f2f3c7a10")


def sid(*parts):
    return str(uuid.uuid5(ID_NAMESPACE, "/".join(parts)))


def audience_mapper():
    return {
        "id": sid("mapper", "knowvera-api-audience"),
        "name": f"{CLIENT_ID} audience",
        "protocol": "openid-connect",
        "protocolMapper": "oidc-audience-mapper",
        "consentRequired": False,
        "config": {"included.client.audience": CLIENT_ID, "access.token.claim": "true",
                   "id.token.claim": "false", "introspection.token.claim": "true"},
    }


def seed_username(role):
    return role.lower().replace("_", ".")


def build_realm(realm, authz, test=False):
    """Apply the generated parts to a realm export (a dict, changed in place)."""
    realm_id = realm.get("id", REALM)
    clients = {c["clientId"]: c for c in realm["clients"]}

    realm_roles = realm.setdefault("roles", {}).setdefault("realm", [])
    by_name = {r["name"]: r for r in realm_roles}
    for role, description in ROLES.items():
        if role in by_name:
            by_name[role]["description"] = description
            by_name[role].setdefault("id", sid("role", role))
            by_name[role].setdefault("containerId", realm_id)
        else:
            realm_roles.append({"id": sid("role", role), "name": role, "description": description,
                                "composite": False, "clientRole": False, "containerId": realm_id,
                                "attributes": {}})

    api = clients[CLIENT_ID]
    api.update({"authorizationServicesEnabled": True, "serviceAccountsEnabled": True,
                "publicClient": False, "authorizationSettings": authz})
    api_roles = realm["roles"].setdefault("client", {}).setdefault(CLIENT_ID, [])
    if not any(r["name"] == "uma_protection" for r in api_roles):
        api_roles.append({"id": sid("client-role", CLIENT_ID, "uma_protection"), "name": "uma_protection",
                          "composite": False, "clientRole": True, "containerId": api.get("id"), "attributes": {}})

    for client_id, secret in CLIENT_SECRETS.items():
        clients[client_id]["secret"] = TEST_SECRET if test else secret

    for client_id in PORTAL_CLIENTS:
        portal = clients[client_id]
        portal.setdefault("attributes", {})["pkce.code.challenge.method"] = "S256"
        # Password grant only for automated tests; the portals use the code flow with PKCE
        portal["directAccessGrantsEnabled"] = test
        mappers = [m for m in portal.get("protocolMappers", []) if m["name"] != audience_mapper()["name"]]
        portal["protocolMappers"] = mappers + [audience_mapper()]
        if test:
            portal["redirectUris"] = ["*"]

    users = realm.setdefault("users", [])
    for user in users:
        if user.get("username") == f"service-account-{CLIENT_ID}":
            user.setdefault("clientRoles", {})[CLIENT_ID] = ["uma_protection"]
        if user.get("username") == f"service-account-{REGISTRATION_CLIENT}":
            user.setdefault("clientRoles", {})["realm-management"] = list(REGISTRATION_SERVICE_ACCOUNT_ROLES)

    if test:
        realm["sslRequired"] = "none"
        default_role = realm.get("defaultRole", {}).get("name", f"default-roles-{REALM}")
        seeded = {seed_username(r) for r in ROLES}
        realm["users"] = [u for u in users if u.get("username") not in seeded] + [{
            "id": sid("user", seed_username(role)),
            "username": seed_username(role),
            "enabled": True,
            "emailVerified": True,
            "firstName": role.split("_")[0].capitalize(),
            "lastName": " ".join(w.capitalize() for w in role.split("_")[1:]) or "User",
            "email": f"{seed_username(role)}@knowvera.test",
            "credentials": [{"type": "password", "value": TEST_PASSWORD, "temporary": False}],
            "realmRoles": [default_role, role],
        } for role in ROLES]
    return realm


def validate_realm(realm):
    """Every reference in the authorization settings must resolve, or the import fails."""
    api = next(c for c in realm["clients"] if c["clientId"] == CLIENT_ID)
    authz = api["authorizationSettings"]
    roles = {r["name"] for r in realm["roles"]["realm"]}
    resources = {r["name"] for r in authz["resources"]}
    scopes = {s["name"] for s in authz["scopes"]}
    policies = {p["name"] for p in authz["policies"]}
    problems = [f"resource {r['name']}: unknown scope {sc['name']}"
                for r in authz["resources"] for sc in r["scopes"] if sc["name"] not in scopes]
    for p in authz["policies"]:
        cfg = p["config"]
        problems += [f"{p['name']}: unknown role {x['id']}" for x in json.loads(cfg.get("roles", "[]"))
                     if x["id"] not in roles]
        problems += [f"{p['name']}: unknown resource {x}" for x in json.loads(cfg.get("resources", "[]"))
                     if x not in resources]
        problems += [f"{p['name']}: unknown scope {x}" for x in json.loads(cfg.get("scopes", "[]"))
                     if x not in scopes]
        problems += [f"{p['name']}: unknown policy {x}" for x in json.loads(cfg.get("applyPolicies", "[]"))
                     if x not in policies]
    ids = [r["id"] for r in realm["roles"]["realm"]]
    if len(ids) != len(set(ids)):
        problems.append("duplicate realm role ids")
    if problems:
        sys.exit("generate_authz.py: realm not written:\n  - " + "\n  - ".join(problems))


def write_realms(authz):
    realm = build_realm(json.loads(REALM_FILE.read_text()), authz)
    validate_realm(realm)
    REALM_FILE.write_text(json.dumps(realm, indent=2))  # same layout as a Keycloak export
    test_realm = build_realm(json.loads(json.dumps(realm)), authz, test=True)
    TEST_REALM_FILE.parent.mkdir(parents=True, exist_ok=True)
    TEST_REALM_FILE.write_text(json.dumps(test_realm, indent=2) + "\n")


def main():
    errors = []
    resources = scan(errors)
    granted = resolve_grants(resources, errors)
    if errors:
        print("generate_authz.py: nothing written, fix these first:", file=sys.stderr)
        for e in errors:
            print("  -", e, file=sys.stderr)
        sys.exit(1)

    authz = build(resources, granted)
    (HERE / "knowvera-api-authz.json").write_text(json.dumps(authz, indent=2) + "\n")
    ENFORCER_FILE.parent.mkdir(parents=True, exist_ok=True)
    ENFORCER_FILE.write_text(json.dumps(build_enforcer(resources, granted), indent=2) + "\n")
    (HERE / "ROLE_MATRIX.md").write_text(build_matrix(resources, granted) + "\n")
    public = write_public_endpoints(resources)

    endpoints = sum(len(r["endpoints"]) for r in resources.values())
    n_perm = sum(1 for p in authz["policies"] if p["type"] == "scope")
    print(f"{len(resources)} resources, {endpoints} endpoints ({len(public)} public), "
          f"{len(authz['scopes'])} scopes, {len(authz['policies']) - n_perm} policies, {n_perm} permissions")

    write_realms(authz)
    print(f"Updated {REALM_FILE.name} and {TEST_REALM_FILE.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
