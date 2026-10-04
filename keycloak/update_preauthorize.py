#!/usr/bin/env python3
"""
Rewrite the andromda_REST_pre_authorize tagged values in the UML model to check the
Keycloak permission authorities (hasAuthority('SCOPE_<resource>:<scope>')) generated from
the role matrix in generate_authz.py (GRANTS), so Spring Security and Keycloak agree.

Model files (both are updated, edits are textual so formatting is preserved):
  - mda/src/main/uml/knowvera.xml   MagicDraw project
  - mda/src/main/uml/knowvera.uml   EMF export read by AndroMDA

Usage:
  python3 keycloak/update_preauthorize.py            dry run, prints the changes
  python3 keycloak/update_preauthorize.py --apply    writes both model files

Rules per operation (endpoint -> resource + scope via generate_authz). Authorities are the
Keycloak permissions loaded by KeycloakPermissionConverter: SCOPE_<resource>:<scope>.
  - Public endpoints (generate_authz.PUBLIC_PATHS): left unchanged.
  - `self` scope: no expression (any authenticated user, the API scopes to the caller).
  - Everyone else needs hasAuthority('SCOPE_<resource>:<scope>'). Keycloak decides which
    roles hold it (GRANTS in generate_authz.py).
  - OWNER_SCOPED roles (APPLICANT, ORG_ADMIN) hold authorities but only act on their own data:
      * reference data (REFERENCE_RESOURCES) or operation in OWNER_BY_ROLE -> the authority
        is enough (OWNER_BY_ROLE operations are reported as needing an ownership check);
      * otherwise -> hasAuthority(...) and (!hasAnyRole(<owner-scoped>) or <ownership clause>),
        or just "and !hasAnyRole(...)" when the operation has no ownership clause.
  - When the matrix grants owner-scoped roles nothing, an existing ownership clause is kept
    as "hasAuthority(...) or <clause>" so owners keep today's access.
"""

import re
import sys
import xml.etree.ElementTree as ET
from collections import defaultdict
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import generate_authz as g  # noqa: E402

UML_DIR = g.ROOT / "mda/src/main/uml"
MODEL_FILES = [UML_DIR / "knowvera.uml", UML_DIR / "knowvera.xml"]
XMI_ID = "{http://www.omg.org/spec/XMI/20131001}id"
OWNER_SCOPED = ["APPLICANT", "ORG_ADMIN"]
# Shared reference data: owner-scoped roles are granted by role, no ownership involved
REFERENCE_RESOURCES = set(g.REFERENCE) | {"settings"}
# Operations without an ownership clause that owner-scoped roles may still call, by role.
# They were unprotected before the role redesign; each needs an ownership check in its
# service or a @kycAuthService clause in the model, after which it can leave this list.
OWNER_BY_ROLE = {
    "BranchApi.findById",
    "BranchApi.findByOrganisation",
    "BranchApi.findByOrganisationPaged",
    "BranchApi.remove",
    "BranchApi.save",
    "ClientRequestApi.downloadRequestTemplate",
    "ClientRequestApi.findByDocument",
    "ClientRequestApi.findByDocumentPaged",
    "ClientRequestApi.findByIndividual",
    "ClientRequestApi.findByIndividualPaged",
    "ClientRequestApi.findByStatus",
    "ClientRequestApi.findByStatusPaged",
    "ClientRequestApi.findUserReadyRequests",
    "ClientRequestApi.findUserReadyRequestsPaged",
    "ClientRequestApi.uploadRequests",
    "ContactApi.findById",
    "ContactApi.findByType",
    "ContactApi.findByTypePaged",
    "ContactApi.save",
    "DocumentApi.downloadFileByUrl",
    "EmploymentRecordApi.findById",
    "EmploymentRecordApi.findByIndividual",
    "EmploymentRecordApi.remove",
    "EmploymentRecordApi.save",
    "KycInvoiceApi.findById",
    "KycInvoiceApi.findByOrganisation",
    "KycInvoiceApi.findByOrganisationPaged",
    "KycInvoiceApi.findBySubscription",
    "KycInvoiceApi.findBySubscriptionPaged",
    "KycInvoiceApi.upload",
    "KycSubscriptionApi.findById",
    "KycSubscriptionApi.findByOrganisation",
    "OrganisationApi.findByRegistrationNo",
    "OrganisationApi.save",
    "OrganisationDocumentApi.findById",
    "OrganisationDocumentApi.findByOrganisation",
    "OrganisationDocumentApi.findByOrganisationPaged",
    "OrganisationDocumentApi.findByStatus",
    "OrganisationDocumentApi.findByStatusPaged",
    "OrganisationDocumentApi.remove",
    "OrganisationDocumentApi.save",
    "UserApi.addRole",
    "UserApi.findByBranchId",
    "UserApi.findByBranchName",
    "UserApi.findByClientRoles",
    "UserApi.findByIdentityNo",
    "UserApi.findByOrganisationId",
    "UserApi.findByOrganisationName",
    "UserApi.findByRealmRoles",
    "UserApi.findUserById",
    "UserApi.loadUsers",
    "UserApi.removeRole",
    "UserApi.saveUser",
    "UserApi.updateUserName",
}

CLAUSE_FIXES = [
    # AndroMDA shortens T(bw.co.knowvera.TargetEntity) to T(TargetEntity), which SpEL cannot resolve
    (r"isTargetRecordOwner\(T\((?:bw\.co\.knowvera\.)?TargetEntity\)\.(\w+),", r"isRecordOwner('\1',"),
    (r"\biskYCRecordOwner\b", "isKycRecordOwner"),
    (r"\bhasOrganisationAccess\b", "isOrganisationUserMatch"),
]


def generated_endpoints():
    """(ApiInterface, method) -> (resource, scope, path) from the generated sources."""
    endpoints = {}
    for file in g.SOURCE_DIRS[0].rglob("*Api.java"):
        text = file.read_text()
        base = g.CLASS_MAPPING.search(text).group(1)
        for m in g.ENDPOINT.finditer(text):
            http, args, _, ret, name = m.groups()
            endpoints[(file.stem, name)] = (g.resource_name(base), g.classify(http.upper(), name, ret),
                                            g.join_path(base, g.mapping_path(args)))
    return endpoints


def model_operations(model):
    """[(base_Operation id, class, operation, current expression)] from the EMF export."""
    root = ET.parse(model).getroot()
    ids = {e.get(XMI_ID): e for e in root.iter() if e.get(XMI_ID)}
    parent = {c: p for p in root.iter() for c in p}
    ops = []
    for e in root.iter():
        if e.tag.endswith("}WebServiceOperation"):
            op = ids[e.get("base_Operation")]
            ops.append((e.get("base_Operation"), parent[op].get("name"), op.get("name"),
                        e.get("andromda_REST_pre_authorize")))
    return ops


def ownership_clause(expression):
    """The @kycAuthService.<method>(...) call in an expression (balanced parentheses), fixed up."""
    if not expression or "@kycAuthService" not in expression:
        return None
    begin = expression.index("@kycAuthService")
    depth, i = 0, expression.index("(", begin)
    for i in range(i, len(expression)):
        depth += {"(": 1, ")": -1}.get(expression[i], 0)
        if depth == 0:
            break
    clause = expression[begin:i + 1]
    for pattern, replacement in CLAUSE_FIXES:
        clause = re.sub(pattern, replacement, clause)
    return clause


def authority(resource, scope):
    return f"hasAuthority('SCOPE_{resource}:{scope}')"


OWNER_CHECK = "!" + "hasAnyRole(" + ", ".join(f"'{r}'" for r in OWNER_SCOPED) + ")"


def plan():
    warnings = []
    resources = g.scan()
    granted = g.resolve_grants(resources, warnings)
    endpoints = generated_endpoints()
    changes, notes = [], defaultdict(list)
    public = [g.path_regex(p) for p in g.PUBLIC_PATHS]

    for op_id, cls, name, current in model_operations(UML_DIR / "knowvera.uml"):
        if (cls, name) not in endpoints:
            notes["not generated, left unchanged"].append(f"{cls}.{name}")
            continue
        resource, scope, path = endpoints[(cls, name)]
        if any(rx.match(path) for rx in public):
            notes["public path (PUBLIC_PATHS), left unchanged"].append(f"{cls}.{name} {path}")
            continue
        if scope == "self":
            new = None
        else:
            roles = granted[(resource, scope)]
            owners = [r for r in OWNER_SCOPED if r in roles]
            clause = ownership_clause(current)
            check = authority(resource, scope)
            by_role = resource in REFERENCE_RESOURCES or f"{cls}.{name}" in OWNER_BY_ROLE
            if owners and by_role:
                if resource not in REFERENCE_RESOURCES and not clause:
                    notes["owner-scoped roles pass on the authority alone (add an ownership check)"].append(
                        f"{cls}.{name} [{resource}:{scope}] {', '.join(owners)}")
                new = check + (f" or {clause}" if clause else "")
            elif owners:
                # Owner-scoped roles hold the authority but must also own the record
                if not clause:
                    notes["owner-scoped roles granted in the matrix but blocked here (no ownership clause)"].append(
                        f"{cls}.{name} [{resource}:{scope}] {', '.join(owners)}")
                new = f"{check} and ({OWNER_CHECK} or {clause})" if clause else f"{check} and {OWNER_CHECK}"
            else:
                if clause:
                    notes["ownership clause lets owners in although the matrix grants them nothing"].append(
                        f"{cls}.{name} [{resource}:{scope}]")
                new = check + (f" or {clause}" if clause else "")
        if new != current:
            changes.append((op_id, f"{cls}.{name}", resource, scope, current, new))
    return changes, notes, warnings


def apply_to_file(path, changes):
    text = path.read_text()
    is_md = path.suffix == ".xml"  # MagicDraw: single quoted attributes, ' escaped as &#39;
    for op_id, label, _, _, current, new in changes:
        tag_rx = re.compile(r"<[\w:]*WebServiceOperation\b[^>]*\bbase_Operation=(['\"])" + re.escape(op_id) + r"\1[^>]*>")
        matches = list(tag_rx.finditer(text))
        if len(matches) != 1:
            raise SystemExit(f"{path.name}: expected one stereotype for {label}, found {len(matches)}")
        tag = matches[0].group(0)
        quote = "'" if is_md else '"'
        attr_rx = re.compile(r"\s+andromda_REST_pre_authorize=(['\"])(.*?)\1")
        if new is None:
            new_tag = attr_rx.sub("", tag)
        else:
            value = new.replace("&", "&amp;").replace("<", "&lt;")
            value = value.replace("'", "&#39;") if quote == "'" else value.replace('"', "&quot;")
            attr = f" andromda_REST_pre_authorize={quote}{value}{quote}"
            if attr_rx.search(tag):
                new_tag = attr_rx.sub(lambda _: attr, tag, count=1)
            else:
                new_tag = re.sub(r"\s*(/?>)$", lambda m: attr + m.group(1), tag)
        text = text[:matches[0].start()] + new_tag + text[matches[0].end():]
    path.write_text(text)


def main():
    changes, notes, warnings = plan()
    for w in warnings:
        print("WARNING:", w)
    for _, label, resource, scope, current, new in changes:
        print(f"{label}  [{resource}:{scope}]")
        print(f"   - {current}")
        print(f"   + {new}")
    for title, items in notes.items():
        print(f"\n{title} ({len(items)}):")
        for item in items:
            print("   ", item)
    print(f"\n{len(changes)} operations to update")

    if "--apply" in sys.argv:
        for path in MODEL_FILES:
            apply_to_file(path, changes)
            print(f"Updated {path.relative_to(g.ROOT)}")


if __name__ == "__main__":
    main()
