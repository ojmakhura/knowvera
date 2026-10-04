#!/usr/bin/env python3
"""
Bring the Angular API services of the portal and admin-portal in line with the REST
controllers of the webservice module.

The services under <app>/src/app/services are generated once by the andromda-angular
cartridge ("CAN EDIT") and then maintained by hand, so they drift when the model changes.
This script compares every service with its Java *Api interface and:
  - updates the base path (protected path = '...');
  - rewrites the path part of each `${this.path}...` URL of an existing method to the Java
    path, keeping the method's signature, body (FormData, blob downloads, ...) and query string;
  - appends methods the API has but the service lacks, and creates services that are missing;
  - reports what it cannot fix safely: HTTP verb and query parameter differences, and TS
    methods without an endpoint.

Existing method signatures are never changed, because callers pass arguments by position.

Usage:
  python3 scripts/sync_angular_services.py                 dry run for portal and admin-portal
  python3 scripts/sync_angular_services.py --apply
  python3 scripts/sync_angular_services.py --apply portal  one app only
"""

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
JAVA_DIRS = [ROOT / "webservice/target/src/main/java", ROOT / "webservice/src/main/java"]
APPS = ["portal", "admin-portal"]

# TS method name -> Java method name, for TS names that differ from the API but are in use
ALIASES = {
    "textExtraction": "textExtration",
    "runVerifications": "runVerification",
    "findClientOrganisationSummariesPaged": "findClientOrganisationSummariesPage",
}

ARGS = r'\((?:"[^"]*"|[^()"]|\([^()]*\))*\)'
ENDPOINT = re.compile(
    r'@(Get|Post|Put|Delete|Patch)Mapping\s*(' + ARGS + r')?'
    r'((?:\s*@\w+(?:' + ARGS + r')?)*)'
    r'\s*public\s+([^;{(]*?)\s*(\w+)\s*\(((?:[^()]|\([^()]*\))*)\)',
    re.S,
)
PARAM = re.compile(r'@(PathVariable|RequestParam|RequestBody|RequestPart)\b(\([^)]*\))?\s*'
                   r'((?:@[\w.]+(?:\([^)]*\))?\s*)*)([\w.<>, ?]+?)\s+(\w+)\s*(?:,|$)')
TS_METHOD = re.compile(r'^([ \t]+)(?:public\s+)?(\w+)\s*\(([^)]*)\)\s*:\s*Observable<', re.M)

JAVA_TO_TS = {
    "String": "string", "Integer": "number", "Long": "number", "Double": "number", "Float": "number",
    "BigDecimal": "number", "int": "number", "long": "number", "double": "number",
    "Boolean": "boolean", "boolean": "boolean", "Object": "any", "Void": "void",
    "MultipartFile": "File", "Instant": "Date", "LocalDate": "Date", "LocalDateTime": "Date",
}
LIBRARY_IMPORTS = {"Page": "@models/page.model", "SearchObject": "@models/search-object"}


def kebab(name):
    """KycRecordApi -> kyc-record-api, AuditLogDTO -> audit-log-dto (the cartridge's file names)."""
    name = re.sub(r"([A-Z]+)([A-Z][a-z])", r"\1-\2", name)
    return re.sub(r"([a-z0-9])([A-Z])", r"\1-\2", name).lower()


# --------------------------------------------------------------------------- Java side
def java_apis():
    apis = {}
    for base_dir in JAVA_DIRS:
        for file in sorted(base_dir.rglob("*Api.java")):
            text = file.read_text()
            if "interface" not in text or file.stem in apis:
                continue
            mapping = re.search(r'^@RequestMapping\(\s*(?:value\s*=\s*)?"([^"]*)"', text, re.M)
            if not mapping:
                continue
            base = "/" + mapping.group(1).strip("/")
            methods = {}
            for m in ENDPOINT.finditer(text):
                http, args, _, ret, name, params = m.groups()
                sub = re.search(r'(?:value|path)\s*=\s*"([^"]*)"', args or "") or re.search(r'^\(\s*"([^"]*)"', args or "")
                sub = sub.group(1) if sub else ""
                path = "/" + "/".join(p.strip("/") for p in (base, sub) if p.strip("/"))
                parsed = []
                for p in PARAM.finditer(params):
                    kind, kargs, _, jtype, var = p.groups()
                    named = re.search(r'(?:name|value)\s*=\s*"([^"]*)"', kargs or "") or re.search(r'^\(\s*"([^"]*)"', kargs or "")
                    required = not re.search(r'required\s*=\s*false', kargs or "")
                    parsed.append({"kind": kind, "name": named.group(1) if named else var, "var": var,
                                   "type": " ".join(jtype.split()), "required": required})
                ret = " ".join(ret.replace("@Nullable", "").split())
                ret = re.sub(r"^ResponseEntity<(.*)>$", r"\1", ret)
                methods[name] = {"http": http.upper(), "path": path, "params": parsed, "ret": ret}
            package = file.relative_to(base_dir).parent
            apis[file.stem] = {"base": base, "methods": methods, "package": package,
                               "imports": dict(re.findall(r"^import ([\w.]+\.(\w+));", text, re.M))}
    return apis


# --------------------------------------------------------------------------- TS types
class Models:
    def __init__(self, app):
        self.index = {}
        for file in (ROOT / app / "src/app/models").rglob("*.ts"):
            rel = file.relative_to(ROOT / app / "src/app/models").with_suffix("")
            self.index.setdefault(file.stem, "@models/" + rel.as_posix())

    def ts_type(self, jtype, imports):
        jtype = jtype.strip()
        generic = re.match(r"^([\w.]+)<(.*)>$", jtype)
        if generic:
            outer, inner = generic.group(1).split(".")[-1], generic.group(2)
            if outer in ("List", "Set", "Collection"):
                return self.ts_type(inner, imports) + "[]"
            if outer in LIBRARY_IMPORTS:
                imports[outer] = LIBRARY_IMPORTS[outer]
                return f"{outer}<{self.ts_type(inner, imports)}>"
            if outer == "Map":
                return "any"
            return "any"
        simple = jtype.split(".")[-1]
        if simple in JAVA_TO_TS:
            return JAVA_TO_TS[simple]
        if kebab(simple) in self.index:
            imports[simple] = self.index[kebab(simple)]
            return simple
        return "any"


# --------------------------------------------------------------------------- TS side
def ts_methods(text):
    found = list(TS_METHOD.finditer(text))
    methods = {}
    for i, m in enumerate(found):
        end = found[i + 1].start() if i + 1 < len(found) else text.rindex("}")
        params = [re.split(r"[?:\s=]", p.strip())[0] for p in m.group(3).split(",") if p.strip()]
        methods[m.group(2)] = {"start": m.start(), "end": end, "params": params}
    return methods


def rewrite_url(template, base, java_path, ts_params, issues, label):
    """`${this.path}<path>?<query>` with the path part replaced by the Java path."""
    rest = template[len("${this.path}"):]
    path_part, sep, query = rest.partition("?")
    interpolations = re.findall(r"\$\{([^}]*)\}", path_part)
    java_vars = list(dict.fromkeys(re.findall(r"\{(\w+)\}", java_path)))
    exprs, unnamed = {}, [v for v in java_vars if v not in ts_params]
    for v in java_vars:
        if v in ts_params:
            exprs[v] = v
    remaining = [e for e in interpolations if e not in exprs.values()]
    if len(remaining) < len(unnamed):
        issues.append(f"{label}: cannot map path variables {unnamed} from {template}")
        return template
    for v, e in zip(unnamed, remaining):
        exprs[v] = e
    sub = java_path[len(base):] if java_path.startswith(base) else java_path
    new_path = re.sub(r"\{(\w+)\}", lambda m: "${" + exprs[m.group(1)] + "}", sub)
    return "${this.path}" + new_path + (sep + query if sep else "")


def generate_method(name, java, models, imports, indent):
    params, body_arg, parts, query = [], None, [], []
    for p in java["params"]:
        ts = models.ts_type(p["type"], imports)
        params.append(f"{p['var']}: {ts if ts == 'any' else ts + ' | any'}")
        if p["kind"] == "RequestBody":
            body_arg = p["var"]
        elif p["kind"] == "RequestPart" or ts in ("File", "File[]"):
            parts.append((p, ts))
        elif p["kind"] == "RequestParam":
            query.append(p)
    ret = models.ts_type(java["ret"], imports) if java["ret"] not in ("void", "") else "void"
    url = re.sub(r"\{(\w+)\}", lambda m: "${" + m.group(1) + "}", java["path"][len(java["base"]):])
    if query:
        url += "?" + "&".join(f"{p['name']}=${{{p['var']}}}" for p in query)
    i2 = indent * 2
    result = ret if ret == "any" else f"{ret} | any"
    lines = [f"{indent}public {name}({', '.join(params)}): Observable<{result}> {{", ""]
    verb = java["http"].lower()
    if parts:
        lines.append(f"{i2}const formData = new FormData();")
        for p, ts in parts:
            if ts == "File[]":
                lines.append(f"{i2}({p['var']} || []).forEach((file: File) => formData.append('{p['name']}', file));")
            elif ts == "File":
                lines.append(f"{i2}formData.append('{p['name']}', {p['var']});")
            else:
                lines.append(f"{i2}formData.append('{p['name']}', new Blob([JSON.stringify({p['var']})], {{ type: 'application/json' }}));")
        lines.append("")
        body_arg = "formData"
    args = f"`${{this.path}}{url}`"
    if verb in ("post", "put", "patch"):
        args += f", {body_arg or 'null'}"
    lines.append(f"{i2}return this.http.{verb}<{result}>({args});")
    lines.append(f"{indent}}}")
    return "\n".join(lines)


def add_imports(text, imports):
    for symbol, module in sorted(imports.items()):
        if re.search(r"import\s*\{[^}]*\b" + symbol + r"\b[^}]*\}", text):
            continue
        last = list(re.finditer(r"^import .*;$", text, re.M))[-1]
        text = text[:last.end()] + f"\nimport {{ {symbol} }} from '{module}';" + text[last.end():]
    return text


def new_service(cls, api, models):
    imports = {}
    indent = "  "
    methods = []
    for name, java in api["methods"].items():
        methods.append(generate_method(name, dict(java, base=api["base"]), models, imports, indent))
    header = [
        "// Synchronised with the webservice by scripts/sync_angular_services.py. CAN EDIT",
        "import { Injectable, inject } from '@angular/core';",
        "import { Observable } from 'rxjs';",
        "import { HttpClient } from '@angular/common/http';",
    ]
    header += [f"import {{ {s} }} from '{m}';" for s, m in sorted(imports.items())]
    return "\n".join(header) + f"""

@Injectable({{
  providedIn: 'root'
}})
export class {cls} {{

  protected path = '{api["base"]}';

  private http = inject(HttpClient);

""" + "\n\n".join(methods) + "\n}\n"


def sync_service(cls, api, ts_file, models, report):
    text = ts_file.read_text()
    original = text
    issues, changes = report["issues"], report["changes"]
    label = f"{ts_file.stem}"

    base_match = re.search(r"(protected\s+path\s*=\s*')([^']*)(')", text)
    old_base = base_match.group(2)
    if old_base != api["base"]:
        text = text[:base_match.start(2)] + api["base"] + text[base_match.end(2):]
        changes.append(f"{label}: base {old_base} -> {api['base']}")

    methods = ts_methods(text)
    matched = set()
    # rewrite from the end so offsets stay valid
    for name, info in sorted(methods.items(), key=lambda kv: -kv[1]["start"]):
        java_name = ALIASES.get(name, name)
        java = api["methods"].get(java_name)
        if java is None:
            issues.append(f"{label}.{name}: no endpoint in {cls}")
            continue
        matched.add(java_name)
        block = text[info["start"]:info["end"]]
        verb = re.search(r"this\.http\.(get|post|put|delete|patch)\b", block)
        if verb and verb.group(1).upper() != java["http"]:
            issues.append(f"{label}.{name}: uses {verb.group(1).upper()}, API expects {java['http']}")

        def fix(m):
            new = rewrite_url(m.group(1), api["base"], java["path"], info["params"], issues, f"{label}.{name}")
            if new != m.group(1):
                changes.append(f"{label}.{name}: {m.group(1)} -> {new}")
            return "`" + new + "`"
        block = re.sub(r"`(\$\{this\.path\}[^`]*)`", fix, block)

        sent = set(re.findall(r"[?&](\w+)=", block)) | set(re.findall(r"append\('(\w+)'", block))
        for options in re.findall(r"params:\s*\{([^}]*)\}", block):  # HttpClient { params: { a, b: x } }
            sent |= set(re.findall(r"(\w+)\s*(?:[:,]|$)", options.strip()))
        wanted = {p["name"] for p in java["params"] if p["kind"] == "RequestParam" and "MultipartFile" not in p["type"]}
        missing = sorted(wanted - sent)
        if missing:
            issues.append(f"{label}.{name}: does not send query parameter(s) {missing}")
        text = text[:info["start"]] + block + text[info["end"]:]

    missing_methods = [n for n in api["methods"] if n not in matched]
    if missing_methods:
        imports = {}
        indent = TS_METHOD.search(text).group(1) if TS_METHOD.search(text) else "  "
        generated = [generate_method(n, dict(api["methods"][n], base=api["base"]), models, imports, indent)
                     for n in missing_methods]
        close = text.rindex("}")
        text = text[:close].rstrip() + "\n\n" + "\n\n".join(generated) + "\n}\n"
        text = add_imports(text, imports)
        changes += [f"{label}: + {n}" for n in missing_methods]
    return text, text != original


def main():
    apply = "--apply" in sys.argv
    apps = [a for a in sys.argv[1:] if not a.startswith("--")] or APPS
    apis = java_apis()
    for app in apps:
        models = Models(app)
        services = ROOT / app / "src/app/services"
        report = {"issues": [], "changes": []}
        for cls, api in apis.items():
            ts_file = services / api["package"] / (kebab(cls) + ".ts")
            if ts_file.exists():
                text, changed = sync_service(cls, api, ts_file, models, report)
            else:
                text, changed = new_service(cls, api, models), True
                report["changes"].append(f"{ts_file.relative_to(ROOT)}: new service")
            if changed and apply:
                ts_file.parent.mkdir(parents=True, exist_ok=True)
                ts_file.write_text(text)
        print(f"===== {app}: {len(report['changes'])} changes")
        for c in report["changes"]:
            print("  ", c)
        print(f"----- {app}: {len(report['issues'])} to check by hand")
        for i in report["issues"]:
            print("  ", i)


if __name__ == "__main__":
    main()
