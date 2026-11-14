#!/usr/bin/env python3
"""
Rewrite the @PreAuthorize expressions of the REST endpoints to check the Keycloak permission
authorities (hasAuthority('SCOPE_<resource>:<scope>')) generated from the role matrix in
generate_authz.py (GRANTS), so Spring Security and Keycloak agree.

Source files (edits are textual, so formatting is preserved):
  - the *Api interfaces (or controllers) declaring the endpoints   @PreAuthorize
  - the methods implementing them (*ApiImpl, or the controller)     @RequiresOwnership

Usage:
  python3 keycloak/update_preauthorize.py            dry run, prints the changes
  python3 keycloak/update_preauthorize.py --apply    rewrites @PreAuthorize and @RequiresOwnership
  python3 keycloak/update_preauthorize.py --verify   fails if the code has an endpoint needing a
                                                     permission without @PreAuthorize

Rules per operation (endpoint -> resource + scope via generate_authz). Authorities are the
Keycloak permissions loaded by KeycloakPermissionConverter: SCOPE_<resource>:<scope>.
  - Public endpoints (generate_authz.PUBLIC_ENDPOINTS): no expression.
  - `self` scope: no expression (any authenticated user, the API scopes to the caller).
  - Everyone else needs hasAuthority('SCOPE_<resource>:<scope>'). Keycloak decides which
    roles hold it (GRANTS in generate_authz.py).
  - Owner-scoped roles (generate_authz.OWNER_SCOPED) hold "<scope>-own" permissions instead
    of "<scope>" (except on reference data), so no expression names a role:
      * @RequiresOwnership on the implementing method -> hasAnyAuthority(<scope>, <scope>-own);
        the KycAuthorisationService aspect requires "-own" holders to own the record;
      * operation in OWNER_BY_ROLE -> hasAnyAuthority(...) without an ownership check (reported);
      * otherwise -> hasAuthority(<scope>), which "-own" holders do not pass.
  - Ownership checks are declared on the implementing method:
    @RequiresOwnership(scope = "<resource>:<scope>", target = "...", id = "#..."); --apply keeps
    its scope (and the stored record check of STORED_RECORD_CHECKS) in line with the endpoint.
  - Migration: an @kycAuthService.<check>(...) clause still in an expression becomes a
    @RequiresOwnership on the implementing method.
"""

import re
import sys
from collections import defaultdict
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import generate_authz as g  # noqa: E402

# Operations without @RequiresOwnership that holders of the "-own" permission may still call.
# They were unprotected before the role redesign; each needs an ownership check (add
# @RequiresOwnership to the implementing method), after which it can leave this list.
OWNER_BY_ROLE = {
    "UserApi.addRole",
    "UserApi.removeRole",
    "UserApi.saveUser",
    "UserApi.updateUserName",
}

OWNERSHIP_IMPORT = "import bw.co.knowvera.auth.RequiresOwnership;"
PRE_AUTHORIZE_IMPORT = "import org.springframework.security.access.prepost.PreAuthorize;"
PRE_AUTHORIZE = re.compile(r'@PreAuthorize\(\s*(value\s*=\s*)?"((?:[^"\\]|\\.)*)"\s*\)')
OWNERSHIP = re.compile(r'RequiresOwnership\(([^)]*)\)')
# OWNER_BY_ROLE operations whose "-own" holders are checked in code instead of by @RequiresOwnership
CHECKED_IN_CODE = {
    "UserApi.addRole": "UserAdministrationGuard",
    "UserApi.removeRole": "UserAdministrationGuard",
    "UserApi.saveUser": "UserAdministrationGuard",
    "UserApi.updateUserName": "UserAdministrationGuard",
}

# Operations that return no record of anyone's, so "-own" holders need no ownership check
NO_RECORD = {
    "ClientRequestApi.downloadRequestTemplate": "the static request spreadsheet template",
}

# Saves whose body names the record's owner: the caller must also own the stored record
# (otherwise naming yourself as owner would take over someone else's record).
STORED_RECORD_CHECKS = {
    "KycRecordApi.save": ("KYC_RECORD", "#kycRecord.id"),
    "DocumentApi.save": ("DOCUMENT", "#document.id"),
    "ClientRequestApi.save": ("CLIENT_REQUEST", "#clientRequest.id"),
}


def ownership_attributes(ownership):
    scope, target, id_expr, record, record_id = ownership
    attrs = f'scope = "{scope}", target = "{target}", id = "{id_expr}"'
    if record_id:
        attrs += f', record = "{record}", recordId = "{record_id}"'
    return attrs


def impl_annotation(ownership):
    return f"@RequiresOwnership({ownership_attributes(ownership)})"


def parse_ownership(text):
    """RequiresOwnership(...) -> (scope or None, target, id, record or None, recordId or None)."""
    m = OWNERSHIP.search(text or "")
    if not m:
        return None
    attrs = dict(re.findall(r'(\w+)\s*=\s*"([^"]*)"', m.group(1)))
    return attrs.get("scope"), attrs["target"], attrs["id"], attrs.get("record"), attrs.get("recordId")

# @kycAuthService.<method> -> TargetEntity it checks (first argument is the id)
CHECK_TARGETS = {
    "isKycRecordOwner": "KYC_RECORD",
    "iskYCRecordOwner": "KYC_RECORD",
    "isIndividualMatch": "INDIVIDUAL",
    "isOrganisationUserMatch": "ORGANISATION",
    "hasOrganisationAccess": "ORGANISATION",
    "isDocumentOwner": "DOCUMENT",
}


def endpoints():
    """(class, method) -> (resource, scope, path, declaring file, implementing file, current expression)."""
    found = {}
    # Every controller, as generate_authz.scan() finds them
    for src in g.SOURCE_DIRS:
        for file in sorted(src.rglob("*.java")):
            text = file.read_text()
            mapping = g.CLASS_MAPPING.search(text)
            if not mapping:
                continue
            base = mapping.group(1)
            impl = file.with_name(file.stem + "Impl.java")
            if not (re.search(r"^public interface\b", text, re.M) and impl.exists()):
                impl = file  # a controller class declares and implements its endpoints
            for m in g.ENDPOINT.finditer(text):
                http, args, annotations, ret, name = m.groups()
                expression = PRE_AUTHORIZE.search(annotations or "")
                current = expression.group(2).replace('\\"', '"').replace("\\\\", "\\") if expression else None
                found[(file.stem, name)] = (g.resource_name(base),
                                            g.classify(f"{file.stem}.{name}", http.upper(), name, ret),
                                            g.join_path(base, g.mapping_path(args)), file, impl, current)
    return found


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
        return None, CHECK_TARGETS[method], args[0], None, None
    if method in ("isTargetRecordOwner", "isRecordOwner"):
        target = args[0]
        constant = re.fullmatch(r"T\((?:bw\.co\.knowvera\.)?TargetEntity\)\.(\w+)|'(\w+)'", target)
        if constant:
            target = constant.group(1) or constant.group(2)
        return None, target, args[1], None, None
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
    changes, annotations, notes = [], [], defaultdict(list)

    for (cls, name), (resource, scope, path, declaring, impl_path, current) in sorted(endpoints().items()):
        label = f"{cls}.{name}"

        if scope == "public":
            new = None
        elif scope == "self":
            new = None
        else:
            # @RequiresOwnership on the implementing method is the source of truth; legacy
            # expression clauses are migrated onto it.
            impl = ImplMethod(impl_path, name)
            clause = ownership_clause(current)
            existing = impl.ownership or (clause_to_ownership(clause) if clause else None)
            ownership = None
            if existing:
                record, record_id = STORED_RECORD_CHECKS.get(label, (existing[3], existing[4]))
                ownership = (f"{resource}:{scope}", existing[1], existing[2], record, record_id)
                impl.check_params([x for x in ownership[1:] if x])
                if impl.ownership != ownership:
                    annotations.append((impl_path, name, ownership))

            own_holders = [r for r in g.OWNER_SCOPED if r in granted[(resource, scope + g.OWN)]]
            check = authority(resource, scope)
            if own_holders and ownership:
                new = either_authority(resource, scope)  # KycAuthorisationService checks ownership
            elif own_holders and label in CHECKED_IN_CODE:
                notes["-own permission checked in code"].append(f"{label}: {CHECKED_IN_CODE[label]}")
                new = either_authority(resource, scope)
            elif own_holders and label in NO_RECORD:
                notes["-own permission on an operation with no record to own"].append(f"{label}: {NO_RECORD[label]}")
                new = either_authority(resource, scope)
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
            changes.append((declaring, name, label, resource, scope, current, new))
    return changes, annotations, notes, warnings


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
            line = f"{m.group(1)}{impl_annotation(ownership)}\n"
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


def java_string(value):
    return '"' + value.replace("\\", "\\\\").replace('"', '\\"') + '"'


def set_pre_authorize(changes):
    """Replace, add or remove the @PreAuthorize above each changed endpoint declaration."""
    by_file = defaultdict(list)
    for declaring, name, _, _, _, _, new in changes:
        by_file[declaring].append((name, new))
    for path, items in by_file.items():
        raw = open(path, newline="").read()
        newline = "\r\n" if "\r\n" in raw else "\n"
        lines = raw.replace("\r\n", "\n").split("\n")
        for name, new in items:
            signature = re.compile(r"^([ \t]*)public\s+[^;{(=]*?\b" + name + r"\s*\(")
            index = next(i for i, l in enumerate(lines) if signature.match(l))
            start = index  # annotations sit between the javadoc / previous member and the signature
            while start > 0 and lines[start - 1].strip() and not lines[start - 1].strip().endswith(("*/", "}", ";", "{")):
                start -= 1
            existing = next((i for i in range(start, index) if PRE_AUTHORIZE.search(lines[i])), None)
            if existing is not None:
                if new is None:
                    del lines[existing]
                else:
                    m = PRE_AUTHORIZE.search(lines[existing])
                    lines[existing] = lines[existing][:m.start()] + \
                        f"@PreAuthorize({m.group(1) or ''}{java_string(new)})" + lines[existing][m.end():]
            elif new is not None:
                # right after the (possibly multi-line) mapping annotation, in the style the file already uses
                at = next(i for i in range(start, index) if re.match(r"\s*@(Get|Post|Put|Delete|Patch)Mapping\b", lines[i]))
                depth = lines[at].count("(") - lines[at].count(")")
                while depth > 0:
                    at += 1
                    depth += lines[at].count("(") - lines[at].count(")")
                style = PRE_AUTHORIZE.search("\n".join(lines))
                value = style.group(1) if style else "value = "
                lines.insert(at + 1, signature.match(lines[index]).group(1) + f"@PreAuthorize({value or ''}{java_string(new)})")
        text = "\n".join(lines)
        uses = "@PreAuthorize(" in text
        if uses and PRE_AUTHORIZE_IMPORT not in text:
            imports = [i for i, l in enumerate(lines) if l.startswith("import ") and not l.startswith("import static ")]
            position = next((i for i in imports if lines[i] > PRE_AUTHORIZE_IMPORT), imports[-1] + 1)
            lines.insert(position, PRE_AUTHORIZE_IMPORT)
        elif not uses and PRE_AUTHORIZE_IMPORT in lines:
            lines.remove(PRE_AUTHORIZE_IMPORT)
        open(path, "w", newline="").write("\n".join(lines).replace("\n", newline))
        print(f"Updated {path.relative_to(g.ROOT)} ({len(items)})")


def unprotected_endpoints():
    """Endpoints that need a permission but have no @PreAuthorize."""
    missing = []
    for src in g.SOURCE_DIRS:
        for file in sorted(src.rglob("*.java")):
            text = file.read_text()
            if not g.CLASS_MAPPING.search(text):
                continue
            for m in g.ENDPOINT.finditer(text):
                http, _, annotations, ret, name = m.groups()
                label = f"{file.stem}.{name}"
                scope = g.classify(label, http.upper(), name, ret)
                if scope not in ("public", "self") and "@PreAuthorize" not in (annotations or ""):
                    missing.append(f"{label} [{scope}]")
    return missing


def main():
    if "--verify" in sys.argv:
        missing = unprotected_endpoints()
        if missing:
            raise SystemExit("Endpoints without @PreAuthorize (run --apply):\n  - "
                             + "\n  - ".join(missing))
        print("All endpoints that need a permission have @PreAuthorize")
        return

    changes, annotations, notes, warnings = plan()
    for w in warnings:
        print("WARNING:", w)
    for _, _, label, resource, scope, current, new in changes:
        print(f"{label}  [{resource}:{scope}]")
        print(f"   - {current}")
        print(f"   + {new}")
    for title, items in notes.items():
        print(f"\n{title} ({len(items)}):")
        for item in items:
            print("   ", item)
    for path, name, ownership in annotations:
        print(f"{path.stem}.{name}: {impl_annotation(ownership)}")
    print(f"\n{len(changes)} expressions to update and {len(annotations)} Impl methods to annotate")

    if "--apply" in sys.argv:
        set_pre_authorize(changes)
        annotate_impls(annotations)


if __name__ == "__main__":
    main()
