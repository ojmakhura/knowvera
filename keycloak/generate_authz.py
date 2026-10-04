#!/usr/bin/env python3
"""
Generate Keycloak authorization settings (resources, scopes, policies and
permissions) for the knowvera-api client from the webservice REST controllers.

Sources scanned:
  - webservice/target/src/main/java   (AndroMDA generated *Api interfaces)
  - webservice/src/main/java          (hand written controllers)

Outputs:
  - keycloak/knowvera-api-authz.json   Import via Clients > knowvera-api > Authorization > Settings > Import
  - keycloak/ROLE_MATRIX.md            Human readable role / resource / scope matrix for review
  - webservice/src/main/resources/keycloak/policy-enforcer.json
        Policy enforcer paths (path + HTTP method -> scope) loaded by SpringSecurityConfig;
        realm, server URL and client credentials are filled in from app.authz.* properties

Options:
  --realm   Also replace the knowvera-api authorizationSettings in ../knowvera-realm.json
            and add any missing ROLES as realm roles.

Design:
  - One resource per controller, named after its @RequestMapping base path.
  - Each endpoint is classified into one SCOPE by its method name (see classify()).
  - Access is defined by GRANTS (role -> resource -> scopes), not by @PreAuthorize.
  - The `self` scope ("my ..." endpoints) is granted to every authenticated user.
  - Keycloak cannot evaluate record ownership or organisation membership, so
    those checks stay in Spring Security (@kycAuthService); Keycloak decides
    whether a role may perform an action on a resource at all.
  - Permissions are scope permissions per (resource, scope), AFFIRMATIVE over
    the role policies granted that scope. A scope nobody is granted gets no
    permission, so Keycloak denies it.
"""

import json
import re
import sys
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

# Mirrors the permitAll() matchers in SpringSecurityConfig
PUBLIC_PATHS = [
    "/swagger-ui/*",
    "/v3/*",
    "/actuator/*",
    "/analytics/*",
    "/client-requests/confirm-token/*",
    "/individual/request/*",
    "/organisations/request/*",
    "/client-requests/{id}/confirm",
]
# Not authorized by Keycloak either: Spring Boot's error page (forwarded to after failures)
ENFORCER_DISABLED_PATHS = PUBLIC_PATHS + ["/error"]

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
SUBJECTS = ["individual", "organisations", "employment", "contacts"]
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
        "individual": READ,
        "kyc-records": ("view", "list", "edit", "submit", "export"),
        "documents": ("view", "list", "edit", "delete", "submit", "export"),
        "subscriptions": READ,
        "invoice": ("view", "list", "submit", "export"),
        "contacts": ("view", "list", "edit"),
        "analytics": ("view",),
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "KYC_ANALYST": {
        "kyc-records": ("view", "list", "edit", "submit", "verify", "export"),
        "kyc-report-sections": ("view", "edit"),
        "documents": ("view", "list", "edit", "submit", "verify", "export"),
        "individual": ("view", "list", "edit", "verify"),
        "organisations": ("view", "list", "verify"),
        "employment": ("view", "list", "edit"),
        "contacts": ("view", "list", "edit"),
        "client-requests": READ,
        "settings": ("view",),
        **grant(REFERENCE, READ),
    },

    "KYC_REVIEWER": {
        "kyc-records": ("view", "list", "verify", "review", "export"),
        "kyc-report-sections": ("view", "edit"),
        "documents": ("view", "list", "verify", "review", "export"),
        "individual": ("view", "list", "verify"),
        "organisations": ("view", "list", "verify"),
        **grant(["employment", "contacts", "client-requests"], READ),
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
        "individual": ("view", "list", "verify"),
        "organisations": ("view", "list", "verify"),
        **grant(["employment", "contacts"], READ),
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
        "individual": ("view", "list", "edit"),
        "organisations": ("view", "list", "edit"),
        "employment": ("view", "list", "edit"),
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
        "individual": ("view", "edit"),
        "organisations": ("view", "edit"),
        "employment": ("view", "list", "edit", "delete"),
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
    "individual": ("view", "list", "verify", "export"),
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


def classify(http, name, return_type):
    for scope, rule in SCOPE_RULES:
        if re.search(rule, name):
            return scope
    if http == "DELETE":
        return "delete"
    if re.search(r"search", name, re.I) or name.startswith("getAll"):
        return "list"
    if http in ("POST", "PUT", "PATCH") and not name.startswith("find"):
        return "edit"
    if re.search(r"\b(List|Page|Collection|Set)<", return_type) or http == "POST":
        return "list"
    return "view"


def resource_name(base):
    return base.strip("/").replace("/", "-") or "root"


def display_name(cls):
    words = re.sub(r"(Api|Controller)$", "", cls)
    return re.sub(r"(?<=[a-z])(?=[A-Z])", " ", words)


def scan():
    resources = {}
    for src in SOURCE_DIRS:
        for file in sorted(src.rglob("*.java")):
            text = file.read_text()
            cls = CLASS_MAPPING.search(text)
            if not cls:
                continue
            base = cls.group(1)
            res = resources.setdefault(resource_name(base), {
                "displayName": display_name(file.stem),
                "endpoints": [],
            })
            for m in ENDPOINT.finditer(text):
                http, args, _, ret, method = m.groups()
                http = http.upper()
                res["endpoints"].append({
                    "method": method,
                    "http": http,
                    "path": join_path(base, mapping_path(args)),
                    "scope": classify(http, method, ret),
                })
    return {k: v for k, v in resources.items() if v["endpoints"]}


def path_regex(pattern):
    parts = re.split(r"(\{[^}]+\}|\*)", pattern)
    rx = "".join("[^/]+" if p.startswith("{") else ".*" if p == "*" else re.escape(p) for p in parts)
    return re.compile("^" + rx + "$")


def scopes_of(resource):
    return [s for s in SCOPES if any(e["scope"] == s for e in resource["endpoints"])]


# --------------------------------------------------------------------------
# Building
# --------------------------------------------------------------------------
def policy_name(role):
    return role.lower().replace("_", "-") + "-policy"


def resolve_grants(resources, warnings):
    """Return {(resource, scope): set(roles)} for scopes that exist on the resource."""
    granted = defaultdict(set)
    available = {n: set(scopes_of(r)) for n, r in resources.items()}
    for role, per_resource in GRANTS.items():
        if role not in ROLES:
            warnings.append(f"GRANTS references unknown role {role}")
        for res, scopes in per_resource.items():
            for s in scopes:
                if s not in SCOPES:
                    warnings.append(f"{role}: unknown scope '{s}' on {res}")
            targets = available if res == "*" else {res: available.get(res)}
            for name, scopes_here in targets.items():
                if scopes_here is None:
                    warnings.append(f"{role}: unknown resource '{name}'")
                    continue
                for s in scopes:
                    if s in scopes_here:
                        granted[(name, s)].add(role)
    return granted


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
        scopes = scopes_of(res)
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
                "description": f"{SCOPES[s]} ({res['displayName']})",
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
        "scopes": [{"name": s, "displayName": d} for s, d in SCOPES.items()],
    }


def build_enforcer(resources):
    by_path = defaultdict(lambda: {"name": None, "methods": defaultdict(set)})
    for name, res in resources.items():
        for e in res["endpoints"]:
            entry = by_path[e["path"]]
            entry["name"] = name
            entry["methods"][e["http"]].add(e["scope"])
    disabled = [path_regex(p) for p in ENFORCER_DISABLED_PATHS]
    paths = [{"path": p, "enforcement-mode": "DISABLED"} for p in ENFORCER_DISABLED_PATHS]
    for path in sorted(by_path):
        if any(rx.match(path) for rx in disabled):
            continue
        entry = by_path[path]
        paths.append({
            "name": entry["name"],
            "path": path,
            "methods": [{"method": m, "scopes": sorted(s)} for m, s in sorted(entry["methods"].items())],
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
        "`self` is granted to every authenticated user and is left out of the table below. "
        "Ownership and organisation membership are enforced by Spring Security "
        "(`@kycAuthService`), not Keycloak.",
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
            cells.append("**all**" if have and have == scopes else ", ".join(have) or "–")
        lines.append(f"| `{name}` | " + " | ".join(cells) + " |")

    lines += ["", "## Endpoints", ""]
    public_rx = [path_regex(p) for p in PUBLIC_PATHS]
    for name in sorted(resources):
        lines += [f"### `{name}`", "", "| Scope | Authority | Endpoint | Method |", "|---|---|---|---|"]
        for e in sorted(resources[name]["endpoints"], key=lambda e: (list(SCOPES).index(e["scope"]), e["path"])):
            flag = " (public)" if any(rx.match(e["path"]) for rx in public_rx) else ""
            authority = "–" if e["scope"] == "self" else f"`SCOPE_{name}:{e['scope']}`"
            lines.append(f"| `{e['scope']}` | {authority} | `{e['http']} {e['path']}`{flag} | `{e['method']}` |")
        lines.append("")
    return "\n".join(lines)


def merge_into_realm(authz):
    realm = json.loads(REALM_FILE.read_text())
    client = next(c for c in realm["clients"] if c["clientId"] == CLIENT_ID)
    client["authorizationServicesEnabled"] = True
    client["authorizationSettings"] = authz
    realm_roles = realm.setdefault("roles", {}).setdefault("realm", [])
    existing = {r["name"] for r in realm_roles}
    added = []
    for role, description in ROLES.items():
        if role not in existing:
            realm_roles.append({
                "name": role,
                "description": description,
                "composite": False,
                "clientRole": False,
                "attributes": {},
            })
            added.append(role)
    REALM_FILE.write_text(json.dumps(realm, indent=2))  # same layout as a Keycloak export
    return added


def main():
    warnings = []
    resources = scan()
    all_paths = [e["path"] for r in resources.values() for e in r["endpoints"]]
    for p in PUBLIC_PATHS:
        if p.startswith(("/swagger-ui", "/v3", "/actuator")):
            continue
        rx = path_regex(p)
        if not any(rx.match(x) for x in all_paths):
            warnings.append(f"public path {p} matches no endpoint")

    granted = resolve_grants(resources, warnings)
    authz = build(resources, granted)
    (HERE / "knowvera-api-authz.json").write_text(json.dumps(authz, indent=2) + "\n")
    ENFORCER_FILE.parent.mkdir(parents=True, exist_ok=True)
    ENFORCER_FILE.write_text(json.dumps(build_enforcer(resources), indent=2) + "\n")
    (HERE / "ROLE_MATRIX.md").write_text(build_matrix(resources, granted) + "\n")

    endpoints = sum(len(r["endpoints"]) for r in resources.values())
    n_perm = sum(1 for p in authz["policies"] if p["type"] == "scope")
    print(f"{len(resources)} resources, {endpoints} endpoints, {len(SCOPES)} scopes, "
          f"{len(authz['policies']) - n_perm} policies, {n_perm} permissions")
    for w in warnings:
        print("WARNING:", w)

    if "--realm" in sys.argv:
        added = merge_into_realm(authz)
        print(f"Updated {REALM_FILE.name}" + (f" (added realm roles: {', '.join(added)})" if added else ""))


if __name__ == "__main__":
    main()
