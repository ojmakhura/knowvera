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
  python3 keycloak/update_preauthorize.py --apply    writes both model files and the Impl annotations

Rules per operation (endpoint -> resource + scope via generate_authz). Authorities are the
Keycloak permissions loaded by KeycloakPermissionConverter: SCOPE_<resource>:<scope>.
  - Public endpoints (generate_authz.PUBLIC_ENDPOINTS): no expression.
  - `self` scope: no expression (any authenticated user, the API scopes to the caller).
  - Everyone else needs hasAuthority('SCOPE_<resource>:<scope>'). Keycloak decides which
    roles hold it (GRANTS in generate_authz.py).
  - Owner-scoped roles (generate_authz.OWNER_SCOPED) hold "<scope>-own" permissions instead
    of "<scope>" (except on reference data), so no expression names a role:
      * @RequiresOwnership on the operation -> hasAnyAuthority(<scope>, <scope>-own); the
        KycAuthorisationService aspect requires "-own" holders to own the record;
      * operation in OWNER_BY_ROLE -> hasAnyAuthority(...) without an ownership check (reported);
      * otherwise -> hasAuthority(<scope>), which "-own" holders do not pass.
  - Ownership checks live in the model as an andromda_additionalAnnotations value on the
    operation: bw.co.knowvera.auth.RequiresOwnership(scope = "<resource>:<scope>", target = "...", id = "#..."), which the
    SpringWSImpl template writes onto the generated Impl method. Impls are only generated
    once, so --apply also adds a missing annotation to the existing Impl method.
  - Migration: a @RequiresOwnership found only on the Impl method, or an
    @kycAuthService.<check>(...) clause still in an expression, is copied into the model.
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
# Operations without @RequiresOwnership that holders of the "-own" permission may still call.
# They were unprotected before the role redesign; each needs an ownership check (add
# @RequiresOwnership to the operation in the model), after which it can leave this list.
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

IMPL_DIR = g.ROOT / "webservice/src/main/java"
OWNERSHIP_IMPORT = "import bw.co.knowvera.auth.RequiresOwnership;"
OWNERSHIP = re.compile(r'RequiresOwnership\(([^)]*)\)')
MODEL_ANNOTATION = 'bw.co.knowvera.auth.RequiresOwnership(scope = "{0}", target = "{1}", id = "{2}")'
IMPL_ANNOTATION = '@RequiresOwnership(scope = "{0}", target = "{1}", id = "{2}")'


def parse_ownership(text):
    """RequiresOwnership(scope = "...", target = "...", id = "...") -> (scope or None, target, id)."""
    m = OWNERSHIP.search(text or "")
    if not m:
        return None
    attrs = dict(re.findall(r'(\w+)\s*=\s*"([^"]*)"', m.group(1)))
    return attrs.get("scope"), attrs["target"], attrs["id"]

# @kycAuthService.<method> -> TargetEntity it checks (first argument is the id)
CHECK_TARGETS = {
    "isKycRecordOwner": "KYC_RECORD",
    "iskYCRecordOwner": "KYC_RECORD",
    "isIndividualMatch": "INDIVIDUAL",
    "isOrganisationUserMatch": "ORGANISATION",
    "hasOrganisationAccess": "ORGANISATION",
    "isDocumentOwner": "DOCUMENT",
}


def generated_endpoints():
    """(ApiInterface, method) -> (resource, scope, path, Impl file) from the generated sources."""
    endpoints = {}
    for file in g.SOURCE_DIRS[0].rglob("*Api.java"):
        text = file.read_text()
        base = g.CLASS_MAPPING.search(text).group(1)
        for m in g.ENDPOINT.finditer(text):
            http, args, _, ret, name = m.groups()
            impl = IMPL_DIR / file.relative_to(g.SOURCE_DIRS[0]).with_name(file.stem + "Impl.java")
            endpoints[(file.stem, name)] = (g.resource_name(base),
                                            g.classify(f"{file.stem}.{name}", http.upper(), name, ret),
                                            g.join_path(base, g.mapping_path(args)), impl)
    return endpoints


def model_operations(model):
    """[(base_Operation id, class, operation, current expression, model @RequiresOwnership)] from the EMF export."""
    root = ET.parse(model).getroot()
    ids = {e.get(XMI_ID): e for e in root.iter() if e.get(XMI_ID)}
    parent = {c: p for p in root.iter() for c in p}
    ops = []
    for e in root.iter():
        if e.tag.endswith("}WebServiceOperation"):
            op = ids[e.get("base_Operation")]
            ownership = None
            for annotation in e.iter("andromda_additionalAnnotations"):
                ownership = parse_ownership(annotation.text) or ownership
            ops.append((e.get("base_Operation"), parent[op].get("name"), op.get("name"),
                        e.get("andromda_REST_pre_authorize"), ownership))
    return ops


def ownership_clause(expression):
    """The @kycAuthService.<method>(...) call in an expression (balanced parentheses)."""
    if not expression or "@kycAuthService" not in expression:
        return None
    begin = expression.index("@kycAuthService")
    depth, i = 0, expression.index("(", begin)
    for i in range(i, len(expression)):
        depth += {"(": 1, ")": -1}.get(expression[i], 0)
        if depth == 0:
            break
    return expression[begin:i + 1]


def split_args(args):
    parts, depth, current = [], 0, ""
    for ch in args:
        if ch == "," and depth == 0:
            parts.append(current.strip())
            current = ""
            continue
        depth += {"(": 1, ")": -1}.get(ch, 0)
        current += ch
    return parts + [current.strip()] if current.strip() else parts


def clause_to_ownership(clause):
    """@kycAuthService.<check>(...) -> (None, target, id) for @RequiresOwnership."""
    m = re.match(r"@kycAuthService\.(\w+)\((.*)\)$", clause)
    method, args = m.group(1), split_args(m.group(2))
    if method in CHECK_TARGETS:
        return None, CHECK_TARGETS[method], args[0]
    if method in ("isTargetRecordOwner", "isRecordOwner"):
        target = args[0]
        constant = re.fullmatch(r"T\((?:bw\.co\.knowvera\.)?TargetEntity\)\.(\w+)|'(\w+)'", target)
        if constant:
            target = constant.group(1) or constant.group(2)
        return None, target, args[1]
    raise SystemExit(f"Cannot convert ownership clause {clause}")


class ImplMethod:
    """The *ApiImpl method implementing an operation, with its @RequiresOwnership if any."""

    def __init__(self, path, name):
        self.path, self.name = path, name
        text = path.read_text()
        self.match = re.search(r"^[ \t]*public\s+[^;{(=]*?\b" + name + r"\s*\(([^)]*)\)", text, re.M)
        if not self.match:
            raise SystemExit(f"{path.name}: method {name} not found")
        params = [p.strip() for p in self.match.group(1).split(",") if p.strip()]
        self.params = {re.findall(r"\w+", p)[-1] for p in params}
        # annotations sit between the previous member and the method signature
        head = text[:self.match.start()]
        previous = max(head.rfind("}"), head.rfind(";"), head.rfind("{"))
        self.ownership = parse_ownership(re.search(r"@RequiresOwnership\([^)]*\)", head[previous + 1:]).group(0)
                                         if "@RequiresOwnership(" in head[previous + 1:] else None)

    def check_params(self, ownership):
        for expression in ownership:
            for var in re.findall(r"#(\w+)", expression):
                if var not in self.params:
                    raise SystemExit(f"{self.path.name}.{self.name}: #{var} is not a parameter "
                                     f"({', '.join(sorted(self.params))})")


def authority(resource, scope):
    return f"hasAuthority('SCOPE_{resource}:{scope}')"


def either_authority(resource, scope):
    return f"hasAnyAuthority('SCOPE_{resource}:{scope}', 'SCOPE_{resource}:{scope}{g.OWN}')"


def plan():
    warnings = []
    resources = g.scan(warnings)
    granted = g.resolve_grants(resources, warnings)
    if warnings:
        raise SystemExit("generate_authz.py rules are incomplete:\n  - " + "\n  - ".join(warnings))
    endpoints = generated_endpoints()
    changes, annotations, model_annotations, notes = [], [], [], defaultdict(list)

    for op_id, cls, name, current, model_ownership in model_operations(UML_DIR / "knowvera.uml"):
        if (cls, name) not in endpoints:
            notes["not generated, left unchanged"].append(f"{cls}.{name}")
            continue
        resource, scope, path, impl_path = endpoints[(cls, name)]
        label = f"{cls}.{name}"

        if scope == "public":
            new = None
        elif scope == "self":
            new = None
        else:
            # The model is the source of truth; Impl annotations and legacy expression
            # clauses are migrated into it. Impls are generated once, so keep them in sync.
            impl = ImplMethod(impl_path, name)
            clause = ownership_clause(current)
            existing = model_ownership or impl.ownership or (clause_to_ownership(clause) if clause else None)
            ownership = None
            if existing:
                ownership = (f"{resource}:{scope}", existing[1], existing[2])
                impl.check_params(ownership[1:])
                if model_ownership != ownership:
                    model_annotations.append((op_id, label, ownership))
                if impl.ownership != ownership:
                    annotations.append((impl_path, name, ownership))
                if impl.ownership and impl.ownership[1:] != existing[1:]:
                    notes["Impl @RequiresOwnership differed from the model (model wins)"].append(
                        f"{label}: model {existing[1:]}, Impl {impl.ownership[1:]}")

            own_holders = [r for r in g.OWNER_SCOPED if r in granted[(resource, scope + g.OWN)]]
            check = authority(resource, scope)
            if own_holders and ownership:
                new = either_authority(resource, scope)  # KycAuthorisationService checks ownership
            elif own_holders and label in OWNER_BY_ROLE:
                notes["-own permission accepted without an ownership check (add @RequiresOwnership)"].append(
                    f"{label} [{resource}:{scope}{g.OWN}] {', '.join(own_holders)}")
                new = either_authority(resource, scope)
            elif own_holders:
                notes["-own permission granted but refused here (no @RequiresOwnership)"].append(
                    f"{label} [{resource}:{scope}{g.OWN}] {', '.join(own_holders)}")
                new = check
            else:
                if ownership:
                    notes["@RequiresOwnership present but nobody holds the -own permission"].append(
                        f"{label} [{resource}:{scope}]")
                new = check
        if new != current:
            changes.append((op_id, label, resource, scope, current, new))
    return changes, annotations, model_annotations, notes, warnings


def annotate_impls(annotations):
    by_file = defaultdict(list)
    for path, name, ownership in annotations:
        by_file[path].append((name, ownership))
    for path, items in by_file.items():
        raw = open(path, newline="").read()
        newline = "\r\n" if "\r\n" in raw else "\n"
        text = raw.replace("\r\n", "\n")
        for name, ownership in items:
            m = re.search(r"^([ \t]*)public\s+[^;{(=]*?\b" + name + r"\s*\(", text, re.M)
            line = f"{m.group(1)}{IMPL_ANNOTATION.format(*ownership)}\n"
            head_start = max(text.rfind("}", 0, m.start()), text.rfind(";", 0, m.start()),
                             text.rfind("{", 0, m.start())) + 1
            head = text[head_start:m.start()]
            existing = re.search(r"^[ \t]*@RequiresOwnership\([^)]*\)[ \t]*\n", head, re.M)
            if existing:
                head = head[:existing.start()] + line + head[existing.end():]
                text = text[:head_start] + head + text[m.start():]
            else:
                text = text[:m.start()] + line + text[m.start():]
        if OWNERSHIP_IMPORT not in text:
            first_import = re.search(r"^import ", text, re.M)
            text = text[:first_import.start()] + OWNERSHIP_IMPORT + "\n" + text[first_import.start():]
        open(path, "w", newline="").write(text.replace("\n", newline))
        print(f"Annotated {path.relative_to(g.ROOT)} ({len(items)})")


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


def annotate_model(path, model_annotations):
    """Add bw.co.knowvera.auth.RequiresOwnership(...) to the operation's andromda_additionalAnnotations."""
    text = path.read_text()
    for op_id, label, ownership in model_annotations:
        tag_rx = re.compile(r"([ \t]*)<([\w:]*WebServiceOperation)\b[^>]*\bbase_Operation=(['\"])"
                            + re.escape(op_id) + r"\3[^>]*?(/?)>", re.M)
        m = tag_rx.search(text)
        if not m:
            raise SystemExit(f"{path.name}: stereotype for {label} not found")
        indent, element, self_closing = m.group(1), m.group(2), m.group(4) == "/"
        child_indent = indent + ("\t" if "\t" in indent else "  ")
        value = MODEL_ANNOTATION.format(*ownership).replace("&", "&amp;").replace("<", "&lt;").replace('"', "&quot;")
        child = f"\n{child_indent}<andromda_additionalAnnotations>{value}</andromda_additionalAnnotations>"
        if not self_closing:
            close = text.index(f"</{element}>", m.end())
            current = re.search(r"<andromda_additionalAnnotations>[^<]*RequiresOwnership[^<]*</andromda_additionalAnnotations>",
                                text[m.end():close])
            if current:
                start, end = m.end() + current.start(), m.end() + current.end()
                text = text[:start] + f"<andromda_additionalAnnotations>{value}</andromda_additionalAnnotations>" + text[end:]
                continue
        if self_closing:
            open_tag = re.sub(r"\s*/>$", ">", m.group(0))
            text = text[:m.start()] + open_tag + child + f"\n{indent}</{element}>" + text[m.end():]
        else:
            close = text.index(f"</{element}>", m.end())
            body_end = text.rindex("\n", m.end(), close)  # keep the closing tag's own indentation
            text = text[:body_end] + child + text[body_end:]
    path.write_text(text)


def main():
    changes, annotations, model_annotations, notes, warnings = plan()
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
    for path, name, ownership in annotations:
        print(f"{path.stem}.{name}: {IMPL_ANNOTATION.format(*ownership)}")
    for _, label, ownership in model_annotations:
        print(f"{label}: model + {MODEL_ANNOTATION.format(*ownership)}")
    print(f"\n{len(changes)} expressions to update, {len(model_annotations)} model operations "
          f"and {len(annotations)} Impl methods to annotate")

    if "--apply" in sys.argv and (changes or model_annotations):
        for path in MODEL_FILES:
            apply_to_file(path, changes)
            annotate_model(path, model_annotations)
            print(f"Updated {path.relative_to(g.ROOT)}")
    if "--apply" in sys.argv:
        annotate_impls(annotations)


if __name__ == "__main__":
    main()
