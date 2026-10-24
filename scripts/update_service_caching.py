#!/usr/bin/env python3
"""
Writes Spring cache annotations for the core services into the UML model (both
mda/src/main/uml/knowvera.uml and knowvera.xml), as andromda_additionalAnnotations tagged values:

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

Run after changing services, DTOs or repositories, then `make build_mda`:

  python3 scripts/update_service_caching.py            dry run, prints the plan
  python3 scripts/update_service_caching.py --apply    writes both model files
"""

import glob
import re
import sys
import uuid
import xml.etree.ElementTree as ET
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
MODELS = [ROOT / "mda/src/main/uml/knowvera.uml", ROOT / "mda/src/main/uml/knowvera.xml"]
XMI_ID = "{http://www.omg.org/spec/XMI/20131001}id"
CACHE_PKG = "org.springframework.cache.annotation."

# Cache of each service (kept from the model where it existed). Services left out are not cached:
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
    "ToolSelector": "settings",
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
    for f in glob.glob(str(ROOT / "common/target/src/main/java/**/*.java"), recursive=True) + \
            glob.glob(str(ROOT / "common/src/main/java/**/*.java"), recursive=True):
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
    for f in glob.glob(str(ROOT / "core/src/main/java/**/*ServiceImpl.java"), recursive=True):
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
    """(service, operation, param names) -> return type, from the generated service interfaces."""
    types = {}
    for f in glob.glob(str(ROOT / "core/target/src/main/java/**/*Service.java"), recursive=True):
        text = Path(f).read_text()
        m = re.search(r"public interface (\w+Service)\b", text)
        if not m:
            continue
        for op in re.finditer(r"^\s*public\s+([\w<>?,. \[\]]+?)\s+(\w+)\(([^)]*)\)", text, re.M):
            names = tuple(p.strip().split()[-1] for p in op.group(3).split(",") if p.strip())
            types[(m.group(1), op.group(2), names)] = op.group(1).strip()
    return types


def operations(model):
    """[(op id, service, operation, in-parameter names)] of the Service classes, from the EMF export."""
    root = ET.parse(model).getroot()
    ids = {e.get(XMI_ID): e for e in root.iter() if e.get(XMI_ID)}
    services = [ids[e.get("base_Class")] for e in root.iter()
                if e.tag.endswith("}Service") and e.get("base_Class") in ids]
    ops, classes = [], {}
    for cls in services:
        classes[cls.get("name")] = cls.get(XMI_ID)
        for op in cls.findall("ownedOperation"):
            params = tuple(p.get("name") for p in op.findall("ownedParameter") if p.get("direction") != "return")
            ops.append((op.get(XMI_ID), cls.get("name"), op.get("name"), params))
    return ops, classes


def caches_list(caches):
    return ", ".join(f'"{c}"' for c in sorted(caches))


def plan():
    holders = embedded_in()
    writes = direct_writes()
    types = return_types()
    ops, classes = operations(MODELS[0])

    class_annotations = {}
    for service, class_id in classes.items():
        cache = CACHES.get(service)
        class_annotations[class_id] = (service, [f'{CACHE_PKG}CacheConfig(cacheNames = "{cache}")'] if cache else [])

    op_annotations = {}
    for op_id, service, name, params in ops:
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
        op_annotations[op_id] = (f"{service}.{name}({', '.join(params)})", annotations)
    return class_annotations, op_annotations, holders, writes


# --------------------------------------------------------------------------- model editing

def escape(value, magicdraw):
    value = value.replace("&", "&amp;").replace("<", "&lt;").replace('"', "&quot;")
    return value.replace("'", "&#39;") if magicdraw else value


def set_annotations(text, element_suffix, base_attr, base_id, annotations, magicdraw, new_elements):
    """Replace the cache annotations of the stereotype application for base_id; True if text changed."""
    tag_rx = re.compile(r"([ \t]*)<([\w:]*" + element_suffix + r")\b[^>]*\b" + base_attr + r"=(['\"])"
                        + re.escape(base_id) + r"\3[^>]*?(/?)>")
    m = tag_rx.search(text)
    indent = "\t" if magicdraw else "  "
    child_indent = indent * 2
    children = "".join(f"\n{child_indent}<andromda_additionalAnnotations>{escape(a, magicdraw)}</andromda_additionalAnnotations>"
                       for a in annotations)
    if not m:
        if not annotations:
            return text
        prefix = "andromda_service" if magicdraw else "andromdaservice"
        q = "'" if magicdraw else '"'
        new_id = (f"_19_0_4_kv_{uuid.uuid4().hex[:12]}_{len(new_elements)}" if magicdraw
                  else "_kv" + uuid.uuid4().hex)
        new_elements.append(f"{indent}<{prefix}:{element_suffix} xmi:id={q}{new_id}{q} {base_attr}={q}{base_id}{q}>"
                            f"{children}\n{indent}</{prefix}:{element_suffix}>\n")
        return text
    element = m.group(2)
    if m.group(4) == "/":
        body, end = "", m.end()
        open_tag = re.sub(r"\s*/>$", ">", m.group(0))
    else:
        close = text.index(f"</{element}>", m.end())
        body, end = text[m.end():close], close + len(f"</{element}>")
        open_tag = m.group(0)
    kept = [c for c in re.findall(r"<andromda_additionalAnnotations>([^<]*)</andromda_additionalAnnotations>", body)
            if not c.startswith(CACHE_PKG)]
    others = re.sub(r"\s*<andromda_additionalAnnotations>[^<]*</andromda_additionalAnnotations>", "", body).strip()
    kept_xml = "".join(f"\n{child_indent}<andromda_additionalAnnotations>{c}</andromda_additionalAnnotations>" for c in kept)
    inner = (f"\n{child_indent}{others}" if others else "") + kept_xml + children
    replacement = (f"{open_tag}{inner}\n{m.group(1)}</{element}>" if inner
                   else re.sub(r">$", "/>", open_tag))
    return text[:m.start()] + replacement + text[end:]


def apply(class_annotations, op_annotations):
    for model in MODELS:
        magicdraw = model.suffix == ".xml"
        text = model.read_text()
        new_elements = []
        for class_id, (_, annotations) in class_annotations.items():
            text = set_annotations(text, "Service", "base_Class", class_id, annotations, magicdraw, new_elements)
        for op_id, (_, annotations) in op_annotations.items():
            text = set_annotations(text, "ServiceOperation", "base_Operation", op_id, annotations, magicdraw, new_elements)
        if new_elements:
            # Next to the existing ServiceOperation applications
            prefix = "andromda_service" if magicdraw else "andromdaservice"
            last = list(re.finditer(r"<" + prefix + r":ServiceOperation\b[^>]*?(?:/>|>.*?</" + prefix
                                    + r":ServiceOperation>)\n", text, re.S))[-1]
            text = text[:last.end()] + "".join(new_elements) + text[last.end():]
        model.write_text(text)
        print(f"Updated {model.relative_to(ROOT)} ({len(new_elements)} new ServiceOperation stereotypes)")


def main():
    class_annotations, op_annotations, holders, writes = plan()
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
        return
    apply(class_annotations, op_annotations)


if __name__ == "__main__":
    main()
