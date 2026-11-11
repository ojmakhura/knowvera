#!/usr/bin/env python3
"""
Writes Spring cache annotations onto the core services (the *ServiceBase classes under
knowvera-api/core/src/main/java):

  service class   @CacheConfig(cacheNames = "<cache>")
  findById(id)    @Cacheable(key = "#id")
  getAll()        @Cacheable(key = "'all'")
  save(x)         @Caching(evict = {@CacheEvict(<caches>, allEntries, beforeInvocation)},
                           put = {@CachePut(key = "#result.id", unless = "#result == null")})
                  (only where save returns what findById returns; otherwise evict only)
  other writes    @CacheEvict(cacheNames = {<caches>}, allEntries = true)
  other reads     not cached (search / paged / counts / token lookups)

<caches> is the service's own cache plus every cache holding a copy of its data: caches whose DTOs
embed this entity's DTO (read from the DTO classes), caches whose findById adds data read from it
(ENRICHED_FROM), and, for entities the service writes directly through other repositories (read from
the ServiceImpls), those entities' caches. The relation is followed transitively.

Run after changing services, DTOs or repositories:

  python3 scripts/update_service_caching.py            dry run, prints the plan and the files it would change
  python3 scripts/update_service_caching.py --apply    rewrites the cache annotations of the *ServiceBase classes
"""

import glob
import re
import sys
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
CORE = ROOT / "knowvera-api/core/src/main/java"
CACHE_PKG = "org.springframework.cache.annotation."
CACHE_ANNOTATIONS = ("CacheConfig", "Cacheable", "CacheEvict", "CachePut", "Caching")

# Cache of each service. Services left out are not cached:
# sequence values change on every use and other services update them directly.
CACHES = {
    "BranchService": "branches",
    "ClientRequestService": "clientRequests",
    "ContactService": "clientContacts",
    "DocumentService": "documents",
    "DocumentTypeService": "documentTypes",
    "EmploymentRecordService": "employmentRecords",
    "ExpectedFieldService": "expectedFields",
    "IndividualService": "individuals",
    "KycFieldGroupService": "kycFieldGroups",
    "KycInvoiceService": "invoices",
    "KycRecordService": "kycRecords",
    "KycReportSectionService": "kycReportSections",
    "KycSubscriptionService": "subscriptions",
    "OrganisationDocumentService": "organisationDocuments",
    "OrganisationService": "organisations",
    "SettingsService": "settings",
    "VerificationDataConfigService": "verificationDataConfigurations",
}

# Entities without a service of their own: the cache whose service owns them
ENTITY_CACHE_ALIASES = {
    "GroupField": "kycFieldGroups",
    "GroupFieldValue": "kycReportSections",
    "SalaryRange": "settings",
    "Declaration": "kycRecords",
}

# findById results that add data looked up elsewhere: cache -> caches it reads from
ENRICHED_FROM = {
    "documentTypes": ["expectedFields", "verificationDataConfigurations"],
    "clientRequests": ["individuals", "organisations"],          # client name
    "documents": ["individuals", "organisations", "kycRecords"],  # target label
}

# save results that differ from findById's (findById enriches them): evict only, no @CachePut
NO_CACHE_PUT = {"documents", "documentTypes", "clientRequests"}

# Further cached lookups: (service, operation) -> key; each key is distinct within its cache
EXTRA_CACHEABLE = {
    ("OrganisationService", "findListOrganisationById"): "'list:' + #id",
    ("OrganisationService", "findListOrganisationByKeycloakId"): "'keycloak:' + #keycloakId",
    ("SettingsService", "loadSettings"): "'one'",
}

READ = re.compile(r"^(find|get|load|count|search|paged|exists|is)")


def cache_of_entity(entity):
    return ENTITY_CACHE_ALIASES.get(entity) or CACHES.get(entity + "Service")


def dto_entity(dto):
    return re.sub(r"(ListDTO|DTO)$", "", dto)


def embedded_in():
    """cache -> caches holding a copy of its data (DTO embedding and findById enrichment)."""
    holders = defaultdict(set)
    for f in glob.glob(str(ROOT / "knowvera-api/common/src/main/java/**/*.java"), recursive=True):
        text = Path(f).read_text()
        m = re.search(r"public (?:abstract )?(?:class|record) (\w+DTO)\b", text)
        if not m:
            continue
        holder = cache_of_entity(dto_entity(m.group(1)))
        if not holder:
            continue
        fields = " ".join(re.findall(r"(?:protected|private)\s+[^;(]+;", text))
        record = re.search(r"record \w+\s*\((.*?)\)\s*(?:implements[^{]*)?\{", text, re.S)
        if record:
            fields += " " + record.group(1)
        for dto in set(re.findall(r"\b([A-Z]\w*DTO)\b", fields)) - {m.group(1)}:
            held = cache_of_entity(dto_entity(dto))
            if held and held != holder:
                holders[held].add(holder)
    for cache, sources in ENRICHED_FROM.items():
        for source in sources:
            holders[source].add(cache)
    return holders


def direct_writes():
    """service -> caches of other entities it saves or deletes through their repositories."""
    writes = defaultdict(set)
    for f in glob.glob(str(CORE / "**/*ServiceImpl.java"), recursive=True):
        text = Path(f).read_text()
        service = Path(f).stem.replace("Impl", "")
        repositories = {m.group(2): m.group(1) for m in re.finditer(r"(\w+)Repository\s+(\w+)\s*[;,)=]", text)}
        for field, entity in repositories.items():
            if re.search(r"\b" + field + r"\.(save|saveAll|saveAndFlush|delete\w*)\(", text):
                cache = cache_of_entity(entity)
                if cache and cache != CACHES.get(service):
                    writes[service].add(cache)
    return writes


def closure(start, holders):
    seen, todo = set(), list(start)
    while todo:
        cache = todo.pop()
        if cache in seen:
            continue
        seen.add(cache)
        todo.extend(holders.get(cache, ()))
    return seen


def return_types():
    """(service, operation, param names) -> return type, from the service interfaces."""
    types = {}
    for f in glob.glob(str(CORE / "**/*Service.java"), recursive=True):
        text = Path(f).read_text()
        m = re.search(r"public interface (\w+Service)\b", text)
        if not m:
            continue
        for op in re.finditer(r"^\s*public\s+([\w<>?,. \[\]]+?)\s+(\w+)\(([^)]*)\)", text, re.M):
            names = tuple(p.strip().split()[-1] for p in op.group(3).split(",") if p.strip())
            types[(m.group(1), op.group(2), names)] = op.group(1).strip()
    return types


SIGNATURE = re.compile(r"^[ \t]*public\s+(?!abstract\b)(?!class\b)[\w<>?,. \[\]]+?\s+(\w+)\(([^)]*)\)", re.M)


def param_names(params):
    return tuple(p.strip().split()[-1] for p in params.split(",") if p.strip())


def service_bases():
    """{service: *ServiceBase file} for the core services."""
    bases = {}
    for f in sorted(CORE.rglob("*ServiceBase.java")):
        m = re.search(r"^public abstract class (\w+Service)Base\b", f.read_text(), re.M)
        if m:
            bases[m.group(1)] = f
    return bases


def operations(bases):
    """[(service, operation, in-parameter names)]: the public operations of each *ServiceBase."""
    ops = []
    for service, f in bases.items():
        for m in SIGNATURE.finditer(f.read_text()):
            ops.append((service, m.group(1), param_names(m.group(2))))
    return ops


def caches_list(caches):
    return ", ".join(f'"{c}"' for c in sorted(caches))


def plan():
    holders = embedded_in()
    writes = direct_writes()
    types = return_types()
    bases = service_bases()

    class_annotations = {}
    for service in bases:
        cache = CACHES.get(service)
        class_annotations[service] = [f'{CACHE_PKG}CacheConfig(cacheNames = "{cache}")'] if cache else []

    op_annotations = {}
    for service, name, params in operations(bases):
        cache = CACHES.get(service)
        annotations = []
        if cache:
            evict = closure({cache} | writes.get(service, set()), holders)
            if (service, name) in EXTRA_CACHEABLE:
                annotations.append(f'{CACHE_PKG}Cacheable(key = "{EXTRA_CACHEABLE[(service, name)]}")')
            elif name == "findById" and params == ("id",):
                annotations.append(f'{CACHE_PKG}Cacheable(key = "#id")')
            elif name == "getAll" and params == ():
                annotations.append(f"{CACHE_PKG}Cacheable(key = \"'all'\")")
            elif READ.match(name):
                pass  # search, paged, counts, token lookups: not cached
            elif name == "save" and cache not in NO_CACHE_PUT and \
                    types.get((service, "save", params)) == types.get((service, "findById", ("id",))):
                annotations.append(
                    f"{CACHE_PKG}Caching(evict = {{ @{CACHE_PKG}CacheEvict(cacheNames = {{{caches_list(evict)}}}, "
                    f"allEntries = true, beforeInvocation = true) }}, put = {{ @{CACHE_PKG}CachePut("
                    f'key = "#result.id", unless = "#result == null") }})')
            else:
                annotations.append(f"{CACHE_PKG}CacheEvict(cacheNames = {{{caches_list(evict)}}}, allEntries = true)")
        op_annotations[(service, name, params)] = (f"{service}.{name}({', '.join(params)})", annotations)
    return bases, class_annotations, op_annotations, holders, writes


# --------------------------------------------------------------------------- source editing

def java(annotation):
    """org.springframework.cache.annotation.X(...) -> @X(...)"""
    return "@" + annotation.replace(CACHE_PKG, "")


def annotation_block(lines, index):
    """Index of the first annotation line above lines[index] (stops at javadoc, blank lines and members)."""
    start = index
    while start > 0:
        above = lines[start - 1].strip()
        if not above or above.endswith(("*/", "}", ";", "{")):
            break
        start -= 1
    return start


def set_annotations(lines, index, annotations):
    """Replace the cache annotations directly above lines[index] with annotations."""
    start = annotation_block(lines, index)
    indent = re.match(r"[ \t]*", lines[index]).group(0)
    kept = [l for l in lines[start:index] if not re.match(r"\s*@(" + "|".join(CACHE_ANNOTATIONS) + r")\b", l)]
    lines[start:index] = kept + [indent + java(a) for a in annotations]


def fix_imports(text):
    """Import exactly the cache annotations the class uses, in sorted position."""
    used = {a for a in CACHE_ANNOTATIONS if re.search(r"@" + a + r"\b", text)}
    lines = [l for l in text.split("\n")
             if not re.fullmatch(r"import " + re.escape(CACHE_PKG) + r"(\w+);", l.strip())]
    imports = [i for i, l in enumerate(lines) if l.startswith("import ") and not l.startswith("import static ")]
    for name in sorted(used):
        statement = f"import {CACHE_PKG}{name};"
        position = next((i for i in imports if lines[i] > statement), imports[-1] + 1)
        lines.insert(position, statement)
        imports = [i for i, l in enumerate(lines) if l.startswith("import ") and not l.startswith("import static ")]
    return "\n".join(lines)


def rewrite(path, service, class_annotations, op_annotations):
    raw = open(path, newline="").read()
    newline = "\r\n" if "\r\n" in raw else "\n"
    lines = raw.replace("\r\n", "\n").split("\n")
    # bottom up, so earlier line numbers stay valid
    for index in reversed(range(len(lines))):
        m = SIGNATURE.match(lines[index])
        if m and (service, m.group(1), param_names(m.group(2))) in op_annotations:
            set_annotations(lines, index, op_annotations[(service, m.group(1), param_names(m.group(2)))][1])
        elif re.match(r"public abstract class " + service + r"Base\b", lines[index]):
            set_annotations(lines, index, class_annotations[service])
    text = fix_imports("\n".join(lines)).replace("\n", newline)
    return raw, text


def apply(bases, class_annotations, op_annotations, write):
    changed = []
    for service, path in bases.items():
        raw, text = rewrite(path, service, class_annotations, op_annotations)
        if text != raw:
            changed.append(path)
            if write:
                open(path, "w", newline="").write(text)
    verb = "Updated" if write else "Would update"
    for path in changed:
        print(f"{verb} {path.relative_to(ROOT)}")
    print(f"{len(changed)} of {len(bases)} service classes {'updated' if write else 'to update'}")


def main():
    bases, class_annotations, op_annotations, holders, writes = plan()
    if "--apply" not in sys.argv:
        print("Caches holding copies of another cache's data:")
        for cache in sorted(holders):
            print(f"  {cache:32} <- {', '.join(sorted(holders[cache]))}")
        print("Direct writes to other entities:")
        for service in sorted(writes):
            print(f"  {service:32} -> {', '.join(sorted(writes[service]))}")
        counts = defaultdict(int)
        for label, annotations in op_annotations.values():
            for a in annotations:
                counts[re.match(re.escape(CACHE_PKG) + r"(\w+)", a).group(1)] += 1
        print("Annotations:", dict(counts))
        for label, annotations in sorted(op_annotations.values()):
            for a in annotations:
                print(f"  {label:70} {a.replace(CACHE_PKG, '')[:150]}")
    apply(bases, class_annotations, op_annotations, "--apply" in sys.argv)


if __name__ == "__main__":
    main()
