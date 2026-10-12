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
  - webservice/.../auth/RoleAssignmentRules.java        who may assign which roles (UserAdministrationGuard)

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
PUBLIC_INFRASTRUCTURE = ["/swagger-ui/**", "/v3/**", "/actuator/health", "/actuator/health/**", "/actuator/info"]

# Action for endpoints whose name does not follow the conventions in classify(), by Class.method
ENDPOINT_SCOPES = {
    # Administrative password reset of any user; users change their own password in Keycloak
    "UserApi.changePassword": "manage",
}

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
    # Only endpoints that act on the caller and take no user / record id as input
    ("self",   r"^(findMy|loadMe$|loadMyOrganisation$|getNovuConfig$)"),
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
# Realm roles, designed as a hierarchy: a role includes every role listed under it in
# COMPOSITES (Keycloak composite roles, so tokens carry the inherited roles too). STAFF and
# CUSTOMER are the bases of the back-office and the external (portal) trees.
ROLES = {
    # ---- external users (portal), limited to their own records ----
    "CUSTOMER":           "Base role of every external user: signs in to the portal, own records only",
    "APPLICANT":          "Individual applying for KYC: own profile, KYC application and documents",
    "ORG_USER":           "Member of a client organisation: its client requests and KYC cases",
    "ORG_ADMIN":          "Administrator of a client organisation: also its users, branches and billing",
    # ---- back office (admin portal) ----
    "STAFF":              "Base role of every back-office user: signs in to the admin portal, reads reference data",
    "KYC_ANALYST":        "Captures KYC data and evidence, runs automated checks",
    "KYC_REVIEWER":       "Reviews captured KYC cases and documents",
    "KYC_APPROVER":       "Takes the approve / reject decision on reviewed KYC cases",
    "SCREENING_ANALYST":  "Screens subjects against sanctions, PEP and adverse media lists",
    "RISK_ANALYST":       "Assesses and records customer risk",
    "AUDITOR":            "Reads and exports everything, including the audit trail",
    "COMPLIANCE_OFFICER": "Oversees KYC compliance and owns the KYC requirements configuration",
    "MLRO":               "Money Laundering Reporting Officer: escalations and regulatory reporting",
    "CASE_MANAGER":       "Routes client requests and KYC cases through the workflow",
    "FINANCE_OFFICER":    "Manages subscriptions and invoices",
    "PLATFORM_ADMIN":     "Configures the platform: settings, reference data, users, sequences",
    "SUPER_ADMIN":        "Everything: platform administration and every business role (bootstrap, support)",
    # ---- both trees ----
    "DEVELOPER":          "Developer / tester: every back-office and external role, to exercise both portals with one account",
}
COMPOSITES = {
    "APPLICANT":          ["CUSTOMER"],
    "ORG_USER":           ["CUSTOMER"],
    "ORG_ADMIN":          ["ORG_USER"],
    "KYC_ANALYST":        ["STAFF"],
    "KYC_REVIEWER":       ["KYC_ANALYST"],
    "KYC_APPROVER":       ["KYC_REVIEWER"],
    "SCREENING_ANALYST":  ["STAFF"],
    "RISK_ANALYST":       ["STAFF"],
    "AUDITOR":            ["STAFF"],
    "COMPLIANCE_OFFICER": ["KYC_APPROVER", "SCREENING_ANALYST", "RISK_ANALYST", "AUDITOR"],
    "MLRO":               ["COMPLIANCE_OFFICER"],
    "CASE_MANAGER":       ["STAFF"],
    "FINANCE_OFFICER":    ["STAFF"],
    "PLATFORM_ADMIN":     ["STAFF"],
    "SUPER_ADMIN":        ["PLATFORM_ADMIN", "MLRO", "CASE_MANAGER", "FINANCE_OFFICER"],
    "DEVELOPER":          ["SUPER_ADMIN", "APPLICANT", "ORG_ADMIN"],
}
# Portal access: a client role on each portal's Keycloak client, included in the base role of
# the users who may use that portal (so every role below it gets access). Each portal's
# route guard requires its role.
PORTAL_ROLES = {
    "admin-portal": ("ADMIN_PORTAL_USER", "STAFF", "May use the back-office admin portal"),
    "knowvera-web": ("PORTAL_USER", "CUSTOMER", "May use the customer portal"),
}


def portal_roles_of(role):
    """Portal client roles a realm role includes directly: {client: [role]}."""
    return {client: [name] for client, (name, base, _) in PORTAL_ROLES.items() if base == role}


# User administration, enforced by UserAdministrationGuard from the generated RoleAssignmentRules.java:
#  - only roles in ROLES may be assigned;
#  - PRIVILEGED_ROLES may only be assigned by, and their holders only managed by, holders of
#    PRIVILEGED_GRANTOR (so a platform administrator cannot take over a super administrator);
#  - callers with only the "-own" user permissions (organisation administrators) manage users of
#    their own organisation and may assign only ORGANISATION_ROLES.
PRIVILEGED_ROLES = ["SUPER_ADMIN", "DEVELOPER"]
PRIVILEGED_GRANTOR = "SUPER_ADMIN"
ORGANISATION_ROLES = ["ORG_USER", "ORG_ADMIN"]
ROLE_RULES_FILE = ROOT / "webservice/src/main/java/bw/co/knowvera/auth/RoleAssignmentRules.java"

# Roles allowed to include both the STAFF and the CUSTOMER tree. They hold the full (staff)
# permissions, so @RequiresOwnership does not limit them to their own records.
BOTH_TREES = {"DEVELOPER"}


def effective_roles(role):
    """The role and every role it includes, directly or indirectly."""
    result, todo = [], [role]
    while todo:
        r = todo.pop()
        if r not in result:
            result.append(r)
            todo += COMPOSITES.get(r, [])
    return result


ALL = tuple(SCOPES)
READ = ("view", "list")
READ_EXPORT = ("view", "list", "export")

REFERENCE = ["document-types", "expected-fields", "kyc-field-groups", "verification-data-configs"]
# Shared reference data: nobody owns it, so owner-scoped roles get the plain scopes there
REFERENCE_RESOURCES = set(REFERENCE) | {"settings"}

# Roles limited to their own records: the external tree. Keycloak grants them "<scope>-own"
# instead of "<scope>" (except on REFERENCE_RESOURCES); @RequiresOwnership lets "<scope>"
# holders through and requires "<scope>-own" holders to own the record.
OWNER_SCOPED = [r for r in ROLES if "CUSTOMER" in effective_roles(r) and "STAFF" not in effective_roles(r)]
OWN = "-own"
SUBJECTS = ["individuals", "organisations", "employment-records", "contacts"]
CASES = ["kyc-records", "kyc-report-sections", "documents", "client-requests"]


def grant(resources, scopes):
    return {r: tuple(scopes) for r in resources}


# What each role adds to the roles it includes (COMPOSITES). The effective grants of a role
# are the union over effective_roles(); ROLE_MATRIX.md shows them.
GRANTS = {
    "CUSTOMER": {
        **grant(REFERENCE, READ),
        "settings": ("view",),
    },
    "APPLICANT": {
        "individuals": ("view", "edit"),
        "employment-records": ("view", "list", "edit", "delete"),
        "contacts": ("view", "edit"),
        "kyc-records": ("view", "edit", "submit"),
        "documents": ("view", "edit", "delete", "submit", "export"),
        "organisations": ("view", "edit"),
        "client-requests": ("view",),
    },
    "ORG_USER": {
        "organisations": READ,
        "organisation-branches": READ,
        "organisation-document-types": READ,
        "client-requests": ("view", "list", "edit", "import", "export"),
        "individuals": READ,
        "kyc-records": ("view", "list", "edit", "submit", "export"),
        "documents": ("view", "list", "edit", "submit", "export"),
        "contacts": ("view", "list", "edit"),
    },
    "ORG_ADMIN": {
        "organisations": ("edit",),
        "organisation-branches": ("edit", "delete"),
        "organisation-document-types": ("edit", "delete"),
        "users": ("view", "list", "edit", "manage"),
        "client-requests": ("delete", "review"),
        "documents": ("delete",),
        "subscriptions": READ,
        "invoices": ("view", "list", "submit", "export"),
    },

    "STAFF": {
        **grant(REFERENCE, READ),
        "settings": ("view",),
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
    },
    "KYC_REVIEWER": {
        "kyc-records": ("review",),
        "documents": ("review",),
    },
    # The approve / reject decision goes through the same status endpoint as a review; the
    # service distinguishes them, so the approver adds no API permission of its own.
    "KYC_APPROVER": {},
    "SCREENING_ANALYST": {
        "kyc-records": ("view", "list", "verify"),
        "kyc-report-sections": ("view", "edit"),
        "documents": ("view", "list", "verify"),
        "individuals": ("view", "list", "verify"),
        "organisations": ("view", "list", "verify"),
        **grant(["employment-records", "contacts"], READ),
    },
    "RISK_ANALYST": {
        "kyc-records": READ_EXPORT,
        "kyc-report-sections": ("view", "edit"),
        "documents": READ,
        **grant(SUBJECTS, READ),
    },
    "AUDITOR": {"*": READ_EXPORT},
    "COMPLIANCE_OFFICER": {
        **grant(REFERENCE, ("edit", "delete")),
        "organisation-document-types": ("edit",),
        "client-requests": ("review",),
        "settings": ("list",),
    },
    # Escalations and regulatory reporting happen in the workflow; at API level the MLRO has
    # everything the compliance officer has.
    "MLRO": {},
    "CASE_MANAGER": {
        "client-requests": ("view", "list", "edit", "delete", "review", "import", "export"),
        "kyc-records": ("view", "list", "edit", "submit", "export"),
        "kyc-report-sections": ("view",),
        "documents": ("view", "list", "submit", "export"),
        **grant(["individuals", "organisations", "employment-records", "contacts"], ("view", "list", "edit")),
        "organisation-branches": READ,
        "users": READ,
    },
    "FINANCE_OFFICER": {
        "subscriptions": ("view", "list", "edit", "delete"),
        "invoices": ("view", "list", "edit", "delete", "submit", "export"),
        "organisations": READ,
    },
    "PLATFORM_ADMIN": {
        "settings": ALL,
        "sequences": ALL,
        **grant(REFERENCE, ALL),
        "users": ALL,
        "organisation-branches": ALL,
        "organisation-document-types": ALL,
        "audit-logs": READ,
    },
    "SUPER_ADMIN": {"*": ALL},
    # Everything comes from SUPER_ADMIN, APPLICANT and ORG_ADMIN
    "DEVELOPER": {},
}

# One sample user per role (username = role in lower case with dots, e.g. kyc.analyst)
SAMPLE_USER_ROLES = [r for r in ROLES if r not in ("STAFF", "CUSTOMER")]


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


def check_roles(errors):
    for name, roles in [("PRIVILEGED_ROLES", PRIVILEGED_ROLES), ("ORGANISATION_ROLES", ORGANISATION_ROLES),
                        ("PRIVILEGED_GRANTOR", [PRIVILEGED_GRANTOR])]:
        errors += [f"{name} references unknown role {r}" for r in roles if r not in ROLES]
    for role, children in COMPOSITES.items():
        for child in [role] + children:
            if child not in ROLES:
                errors.append(f"COMPOSITES references unknown role {child}")
    for role in ROLES:
        chain = effective_roles(role)
        if "STAFF" in chain and "CUSTOMER" in chain and role not in BOTH_TREES:
            errors.append(f"{role} includes both STAFF and CUSTOMER: external roles are limited to own records")
        if role in [r for c in COMPOSITES.get(role, []) for r in effective_roles(c)]:
            errors.append(f"{role} includes itself (cycle in COMPOSITES)")


def resolve_grants(resources, errors):
    """Return {(resource, scope): set(roles)} for scopes that exist on the resource.

    Only roles granted directly are listed; Keycloak role policies match composite roles,
    so a role also passes the permissions of the roles it includes."""
    check_roles(errors)
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
        "CUSTOMER": "CUS", "APPLICANT": "APP", "ORG_USER": "OU", "ORG_ADMIN": "OA", "STAFF": "STF",
        "KYC_ANALYST": "KAN", "KYC_REVIEWER": "KRV", "KYC_APPROVER": "KAP", "SCREENING_ANALYST": "SA",
        "RISK_ANALYST": "RA", "AUDITOR": "AUD", "COMPLIANCE_OFFICER": "CO", "MLRO": "MLRO",
        "CASE_MANAGER": "CM", "FINANCE_OFFICER": "FO", "PLATFORM_ADMIN": "PA", "SUPER_ADMIN": "SUP",
        "DEVELOPER": "DEV",
    }

    def holds(role, name, scope):
        """Effective grant: the role or a role it includes was granted the scope."""
        return any(r in granted.get((name, scope), ()) for r in effective_roles(role))
    lines = [
        "# knowvera-api role matrix",
        "",
        "Generated by `keycloak/generate_authz.py`. Edit `GRANTS` / `SCOPE_RULES` there, not this file.",
        "",
        "## Roles",
        "",
        "Roles are hierarchical: a role has everything the roles it includes have (Keycloak composite roles).",
        "",
        "| Role | Abbrev. | Includes | Description |",
        "|---|---|---|---|",
        *[f"| `{r}` | {abbrev[r]} | "
          f"{', '.join([f'`{c}`' for c in COMPOSITES.get(r, [])] + [f'`{c}` ({k} client role)' for k, v in portal_roles_of(r).items() for c in v]) or '–'}"
          f" | {d} |" for r, d in ROLES.items()],
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
        "## Resource × role (effective, including inherited grants)",
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
            have = [s for s in scopes if holds(role, name, s)]
            own = [s for s in scopes if holds(role, name, s + OWN)]
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


def write_role_rules():
    def java_list(values):
        return ", ".join(f'"{v}"' for v in values)

    ROLE_RULES_FILE.write_text(f"""package bw.co.knowvera.auth;

import java.util.Set;

/**
 * Which roles may be assigned, and by whom (see UserAdministrationGuard).
 * <p>
 * Generated by {{@code keycloak/generate_authz.py}} from ROLES, PRIVILEGED_ROLES, PRIVILEGED_GRANTOR
 * and ORGANISATION_ROLES; do not edit by hand.
 */
public final class RoleAssignmentRules {{

    /** Every application role (realm roles). Nothing else may be assigned. */
    public static final Set<String> ROLES = Set.of({java_list(ROLES)});

    /** Only holders of {{@link #PRIVILEGED_GRANTOR}} may assign these, or manage users who hold them. */
    public static final Set<String> PRIVILEGED_ROLES = Set.of({java_list(PRIVILEGED_ROLES)});

    public static final String PRIVILEGED_GRANTOR = "{PRIVILEGED_GRANTOR}";

    /** The only roles organisation administrators may assign, within their own organisation. */
    public static final Set<String> ORGANISATION_ROLES = Set.of({java_list(ORGANISATION_ROLES)});

    private RoleAssignmentRules() {{
    }}
}}
""")


# --------------------------------------------------------------------------
# Realm. knowvera-realm.json is a full Keycloak export (client scopes, flows, SMTP, ...);
# the generator owns the parts below and leaves the rest as exported.
# --------------------------------------------------------------------------
TEST_REALM_FILE = ROOT / "webservice/src/test/resources/test-realm.json"
REGISTRATION_CLIENT = "knowvera-registration"
PORTAL_CLIENTS = list(PORTAL_ROLES)
# Resolved from the environment when Keycloak imports the realm at start-up (--import-realm)
CLIENT_SECRETS = {CLIENT_ID: "${API_CLIENT_SECRET}", REGISTRATION_CLIENT: "${REGISTRATION_CLIENT_SECRET}"}
# KeycloakService.createRegistrationKeycloak() uses this service account to create users, reset
# passwords, map roles and manage Keycloak Organizations (manage-realm: no narrower role exists).
REGISTRATION_SERVICE_ACCOUNT_ROLES = ["manage-users", "manage-realm"]
TEST_SECRET = "test-secret"
TEST_PASSWORD = "Test-Passw0rd!"  # meets PASSWORD_POLICY
SAMPLE_PASSWORD = "ChangeMe-2026!"  # temporary: Keycloak asks for a new one at first login

# Sign-in protection. Keep the length in step with app.security.password.min-length
# (MIN_PASSWORD_LENGTH), which sizes the passwords the API generates for new users.
PASSWORD_POLICY = ("length(12) and upperCase(1) and lowerCase(1) and digits(1) and specialChars(1) "
                   "and notUsername(undefined) and notEmail(undefined) and passwordHistory(5)")
BRUTE_FORCE = {
    "bruteForceProtected": True,
    "permanentLockout": False,
    "failureFactor": 5,                  # failed sign-ins before a temporary lockout
    "waitIncrementSeconds": 60,          # first lockout; grows with further failures
    "maxFailureWaitSeconds": 900,        # longest lockout
    "maxDeltaTimeSeconds": 43200,        # failures are forgotten after 12 hours
    "minimumQuickLoginWaitSeconds": 60,
    "quickLoginCheckMilliSeconds": 1000,
}

# Stable ids, so re-imports update rather than duplicate
ID_NAMESPACE = uuid.UUID("5f0c3a52-4f0e-4c41-9b0f-6b1f2f3c7a10")


def sid(*parts):
    return str(uuid.uuid5(ID_NAMESPACE, "/".join(parts)))


def audience_mapper(client_id):
    return {
        "id": sid("mapper", client_id, "knowvera-api-audience"),  # ids are realm-wide unique
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

    # Realm roles: exactly ROLES (with their hierarchy) plus Keycloak's own roles
    default_role = realm.get("defaultRole", {}).get("name", f"default-roles-{REALM}")
    builtin = {default_role, "offline_access", "uma_authorization"}
    roles = realm.setdefault("roles", {})
    existing = {r["name"]: r for r in roles.get("realm", [])}
    roles["realm"] = [existing[n] for n in existing if n in builtin] + [{
        "id": existing.get(role, {}).get("id", sid("role", role)),
        "name": role,
        "description": description,
        "composite": role in COMPOSITES or bool(portal_roles_of(role)),
        **({"composites": {**({"realm": COMPOSITES[role]} if role in COMPOSITES else {}),
                           **({"client": portal_roles_of(role)} if portal_roles_of(role) else {})}}
           if role in COMPOSITES or portal_roles_of(role) else {}),
        "clientRole": False,
        "containerId": realm_id,
        "attributes": {},
    } for role, description in ROLES.items()]
    # Portal clients: only their access role (granted through STAFF / CUSTOMER)
    for client_id, (name, base, description) in PORTAL_ROLES.items():
        roles.setdefault("client", {})[client_id] = [{
            "id": sid("client-role", client_id, name), "name": name, "description": description,
            "composite": False, "clientRole": True, "containerId": clients[client_id].get("id"),
            "attributes": {}}]

    api = clients[CLIENT_ID]
    api.update({"authorizationServicesEnabled": True, "serviceAccountsEnabled": True,
                "publicClient": False, "authorizationSettings": authz})
    api_roles = [r for r in roles.setdefault("client", {}).get(CLIENT_ID, []) if r["name"] == "uma_protection"]
    roles["client"][CLIENT_ID] = api_roles or [{
        "id": sid("client-role", CLIENT_ID, "uma_protection"), "name": "uma_protection", "composite": False,
        "clientRole": True, "containerId": api.get("id"), "attributes": {}}]

    for client_id, secret in CLIENT_SECRETS.items():
        clients[client_id]["secret"] = TEST_SECRET if test else secret

    for client_id in PORTAL_CLIENTS:
        portal = clients[client_id]
        portal.setdefault("attributes", {})["pkce.code.challenge.method"] = "S256"
        # Password grant only for automated tests; the portals use the code flow with PKCE
        portal["directAccessGrantsEnabled"] = test
        mapper = audience_mapper(client_id)
        mappers = [m for m in portal.get("protocolMappers", []) if m["name"] != mapper["name"]]
        portal["protocolMappers"] = mappers + [mapper]
        if test:
            portal["redirectUris"] = ["*"]

    users = realm.setdefault("users", [])
    for user in users:
        if user.get("username") == f"service-account-{CLIENT_ID}":
            user.setdefault("clientRoles", {})[CLIENT_ID] = ["uma_protection"]
        if user.get("username") == f"service-account-{REGISTRATION_CLIENT}":
            user.setdefault("clientRoles", {})["realm-management"] = list(REGISTRATION_SERVICE_ACCOUNT_ROLES)

    # Sample users, one per role. In the deployable realm the password must be changed at
    # first login; the test realm uses a fixed password.
    sample = {seed_username(r) for r in SAMPLE_USER_ROLES}
    realm["users"] = [u for u in users if u.get("username") not in sample] + [{
        "id": sid("user", seed_username(role)),
        "username": seed_username(role),
        "enabled": True,
        "emailVerified": True,
        "firstName": role.split("_")[0].capitalize(),
        "lastName": " ".join(w.capitalize() for w in role.split("_")[1:]) or "User",
        "email": f"{seed_username(role)}@knowvera.test",
        "credentials": [{"type": "password", "value": TEST_PASSWORD if test else SAMPLE_PASSWORD,
                         "temporary": not test}],
        "realmRoles": [default_role, role],
    } for role in SAMPLE_USER_ROLES]

    realm["passwordPolicy"] = PASSWORD_POLICY
    realm.update(BRUTE_FORCE)

    if test:
        realm["sslRequired"] = "none"
        # Tests sign in with the shared sample users; a test of failed sign-ins must not lock them out
        realm["bruteForceProtected"] = False
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
    realm_roles = {r["name"] for r in realm["roles"]["realm"]}
    for r in realm["roles"]["realm"]:
        problems += [f"role {r['name']} includes unknown role {c}"
                     for c in r.get("composites", {}).get("realm", []) if c not in realm_roles]
        for client_id, names in r.get("composites", {}).get("client", {}).items():
            defined = {x["name"] for x in realm["roles"].get("client", {}).get(client_id, [])}
            problems += [f"role {r['name']} includes unknown {client_id} role {n}" for n in names if n not in defined]
    for u in realm.get("users", []):
        problems += [f"user {u['username']} has unknown role {r}" for r in u.get("realmRoles", []) if r not in realm_roles]
        for client_id, names in u.get("clientRoles", {}).items():
            defined = {r["name"] for r in realm["roles"].get("client", {}).get(client_id, [])}
            problems += [f"user {u['username']} has unknown {client_id} role {n}" for n in names
                         if client_id in realm["roles"].get("client", {}) and n not in defined]
    for g_ in realm.get("groups", []):
        problems += [f"group {g_['name']} has unknown role {r}" for r in g_.get("realmRoles", []) if r not in realm_roles]

    # Keycloak stores ids as primary keys: a repeat fails the import (defaultRole only refers to a role)
    seen = defaultdict(list)

    def collect(node, where):
        if isinstance(node, dict):
            if isinstance(node.get("id"), str):
                seen[node["id"]].append(f"{where} {node.get('name') or node.get('clientId') or node.get('username') or ''}")
            for key, value in node.items():
                if key != "defaultRole":
                    collect(value, f"{where}/{key}")
        elif isinstance(node, list):
            for item in node:
                collect(item, where)

    collect(realm, "")
    problems += [f"id {i} used by {', '.join(places)}" for i, places in seen.items() if len(places) > 1]
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
    write_role_rules()

    endpoints = sum(len(r["endpoints"]) for r in resources.values())
    n_perm = sum(1 for p in authz["policies"] if p["type"] == "scope")
    print(f"{len(resources)} resources, {endpoints} endpoints ({len(public)} public), "
          f"{len(authz['scopes'])} scopes, {len(authz['policies']) - n_perm} policies, {n_perm} permissions")

    write_realms(authz)
    print(f"Updated {REALM_FILE.name} and {TEST_REALM_FILE.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
