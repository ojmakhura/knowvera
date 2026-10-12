#!/usr/bin/env python3
"""
Generate JMeter test plans for the knowvera API from the webservice controllers, the role
matrix (keycloak/generate_authz.py) and the generated @PreAuthorize expressions.

    python3 scripts/generate_jmeter.py

Writes:
  jmeter/knowvera-authorization.jmx   every endpoint, for every sample user, against the
                                       expected outcome of the permission rules (+ public / 401 checks)
  jmeter/knowvera-load.jmx            read-heavy journeys of staff, applicants and organisations

Both plans sign in through Keycloak's password grant with the sample users of
webservice/src/test/resources/test-realm.json, so they run against a Keycloak with that realm
(the deployable realm disables the password grant on the portals). Only read (GET) endpoints are
called on the "allowed" side, so the authorization plan does not change data; writes are only
called where they must be refused. The JMX uses elements available from JMeter 2.13 to 5.x.
Run again after the API or the roles change.
"""

import re
import sys
from collections import defaultdict
from pathlib import Path
from xml.sax.saxutils import escape

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT / "keycloak"))
sys.path.insert(0, str(ROOT / "scripts"))
import generate_authz as g  # noqa: E402
import sync_angular_services as ng  # noqa: E402
import update_preauthorize as up  # noqa: E402

OUT = ROOT / "jmeter"
COMMON = ROOT / "common/target/src/main/java"

PROPERTIES = [
    ("protocol", "http", "API protocol"),
    ("host", "localhost", "API host"),
    ("port", "8085", "API port"),
    ("keycloakUrl", "http://localhost:8970", "Keycloak base URL"),
    ("realm", g.REALM, "Keycloak realm"),
    ("password", g.TEST_PASSWORD, "Password of the sample users (test realm)"),
    ("unknownId", "00000000-0000-4000-8000-000000000000", "Id of a record that does not exist"),
]
LOAD_PROPERTIES = [
    ("staffThreads", "5", "Concurrent back-office users (KYC analyst journey)"),
    ("applicantThreads", "5", "Concurrent applicants"),
    ("orgThreads", "3", "Concurrent organisation administrators"),
    ("rampUp", "60", "Seconds to start all threads"),
    ("duration", "600", "Test length in seconds (keep below the access token lifetime, 900 s)"),
    ("thinkMin", "500", "Minimum think time between requests (ms)"),
    ("thinkMax", "2000", "Maximum think time between requests (ms)"),
]


# --------------------------------------------------------------------------- endpoint model
def enum_first_constants():
    constants = {}
    for f in COMMON.rglob("*.java"):
        text = f.read_text()
        if re.search(r"public enum " + f.stem + r"\b", text):
            m = re.search(r"^\s*([A-Z][A-Z0-9_]*)\s*[(,;]", text[text.index("public enum"):], re.M)
            if m:
                constants[f.stem] = m.group(1)
    return constants


def expressions():
    """(Class, method) -> @PreAuthorize expression ('' when none), from the generated/hand-written code."""
    found = {}
    for src in g.SOURCE_DIRS:
        for file in src.rglob("*.java"):
            text = file.read_text()
            if not g.CLASS_MAPPING.search(text):
                continue
            for m in g.ENDPOINT.finditer(text):
                annotations, name = m.group(3) or "", m.group(5)
                pre = re.search(r'@PreAuthorize\(\s*(?:value\s*=\s*)?"([^"]*)"', annotations)
                found[(file.stem, name)] = pre.group(1) if pre else ""
    return found


def ownership_checked():
    """(Class, method) of endpoints whose Impl carries @RequiresOwnership."""
    checked = set()
    for (cls, name), (_, _, _, impl) in up.generated_endpoints().items():
        if impl.exists() and up.ImplMethod(impl, name).ownership:
            checked.add((cls, name))
    return checked


def build_endpoints():
    errors = []
    resources = g.scan(errors)
    granted = g.resolve_grants(resources, errors)
    if errors:
        sys.exit("generate_authz.py rules are incomplete:\n  - " + "\n  - ".join(errors))
    apis = ng.java_apis()
    exprs = expressions()
    owned = ownership_checked()
    endpoints = []
    for resource, res in sorted(resources.items()):
        for e in res["endpoints"]:
            cls, name = e["label"].split(".")
            java = apis.get(cls, {}).get("methods", {}).get(name, {"params": []})
            expression = exprs.get((cls, name), "")
            endpoints.append({
                **e, "cls": cls, "name": name, "resource": resource,
                "params": java["params"],
                "accepted": re.findall(r"'SCOPE_([^']+)'", expression),
                "ownership": (cls, name) in owned,
            })
    return endpoints, granted


def holds(role, permission, granted):
    resource, scope = permission.split(":", 1)
    return any(r in granted.get((resource, scope), ()) for r in g.effective_roles(role))


def expectation(role, endpoint, granted):
    """('allow' | 'deny', reason) for a call by a sample user with this role."""
    if endpoint["scope"] == "self":
        return "allow", "own data of any signed-in user"
    accepted = endpoint["accepted"]
    if not accepted:
        return "deny", "no @PreAuthorize found: unexpected"
    plain = [p for p in accepted if not p.endswith(g.OWN) and holds(role, p, granted)]
    own = [p for p in accepted if p.endswith(g.OWN) and holds(role, p, granted)]
    if plain:
        return "allow", f"holds {plain[0]}"
    if own and endpoint["ownership"]:
        return "deny", f"holds only {own[0]}; the record is not theirs"
    if own:
        return "allow", f"holds {own[0]} (no ownership check on this endpoint)"
    return "deny", f"lacks {' / '.join(accepted)}"


# --------------------------------------------------------------------------- request building
def value_for(param, enums):
    jtype = param["type"].split("<")[0].split(".")[-1]
    name = param["name"].lower()
    if name == "pagesize":
        return "10"
    if name == "pagenumber":
        return "0"
    if jtype in enums:
        return enums[jtype]
    if jtype in ("Integer", "Long", "int", "long", "Double", "BigDecimal"):
        return "0"
    if jtype in ("Boolean", "boolean"):
        return "true"
    if name.endswith("id") or name in ("id",):
        return "${unknownId}"
    return "test"


def request_for(endpoint, enums):
    """(path with query, body or None, multipart?) for the endpoint."""
    path = endpoint["path"]
    query, body, multipart = [], None, False
    for p in endpoint["params"]:
        if p["kind"] == "PathVariable":
            path = path.replace("{" + p["name"] + "}", value_for(p, enums))
        elif p["kind"] == "RequestParam" and "MultipartFile" not in p["type"]:
            if p["required"] or p["name"] in ("pageNumber", "pageSize"):
                query.append(f"{p['name']}={value_for(p, enums)}")
        elif p["kind"] == "RequestBody":
            body = "{}"
        elif p["kind"] == "RequestPart" or "MultipartFile" in p["type"]:
            multipart = True
    path = re.sub(r"\{[^}]+\}", "${unknownId}", path)  # any variable not named like its parameter
    return path + ("?" + "&".join(query) if query else ""), body, multipart


# --------------------------------------------------------------------------- JMX writing
def x(text):
    return escape(str(text), {'"': "&quot;"})


def prop(kind, name, value):
    return f'<{kind} name="{name}">{x(value)}</{kind}>'


def arguments(pairs, http=False):
    items = []
    for name, value in pairs:
        if http:
            items.append(f'<elementProp name="{x(name)}" elementType="HTTPArgument">'
                         f'{prop("boolProp", "HTTPArgument.always_encode", "true")}'
                         f'{prop("stringProp", "Argument.value", value)}'
                         f'{prop("stringProp", "Argument.metadata", "=")}'
                         f'{prop("boolProp", "HTTPArgument.use_equals", "true")}'
                         f'{prop("stringProp", "Argument.name", name)}</elementProp>')
        else:
            items.append(f'<elementProp name="{x(name)}" elementType="Argument">'
                         f'{prop("stringProp", "Argument.name", name)}{prop("stringProp", "Argument.value", value)}'
                         f'{prop("stringProp", "Argument.metadata", "=")}</elementProp>')
    return "".join(items)


def node(element, children=()):
    """An element followed by its hashTree of children (JMX layout)."""
    return element + "<hashTree>" + "".join(children) + "</hashTree>"


def test_plan(name, comment, variables, children, serial=True):
    plan = (f'<TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="{x(name)}" enabled="true">'
            f'{prop("stringProp", "TestPlan.comments", comment)}'
            f'{prop("boolProp", "TestPlan.functional_mode", "false")}'
            f'{prop("boolProp", "TestPlan.serialize_threadgroups", "true" if serial else "false")}'
            '<elementProp name="TestPlan.user_defined_variables" elementType="Arguments" guiclass="ArgumentsPanel" '
            'testclass="Arguments" testname="User Defined Variables" enabled="true">'
            f'<collectionProp name="Arguments.arguments">{arguments(variables)}</collectionProp></elementProp>'
            f'{prop("stringProp", "TestPlan.user_define_classpath", "")}</TestPlan>')
    return ('<?xml version="1.0" encoding="UTF-8"?>\n'
            '<jmeterTestPlan version="1.2" properties="2.8" jmeter="2.13">'
            + "<hashTree>" + node(plan, children) + "</hashTree></jmeterTestPlan>\n")


def http_defaults():
    return node('<ConfigTestElement guiclass="HttpDefaultsGui" testclass="ConfigTestElement" testname="API" enabled="true">'
                '<elementProp name="HTTPsampler.Arguments" elementType="Arguments" guiclass="HTTPArgumentsPanel" '
                'testclass="Arguments" enabled="true"><collectionProp name="Arguments.arguments"/></elementProp>'
                f'{prop("stringProp", "HTTPSampler.domain", "${host}")}{prop("stringProp", "HTTPSampler.port", "${port}")}'
                f'{prop("stringProp", "HTTPSampler.protocol", "${protocol}")}'
                f'{prop("stringProp", "HTTPSampler.contentEncoding", "UTF-8")}'
                f'{prop("stringProp", "HTTPSampler.connect_timeout", "5000")}'
                f'{prop("stringProp", "HTTPSampler.response_timeout", "30000")}</ConfigTestElement>')


def thread_group(name, children, threads="1", loops="1", ramp="0", duration=None, comment=""):
    forever = "true" if loops == "-1" else "false"
    return node(
        f'<ThreadGroup guiclass="ThreadGroupGui" testclass="ThreadGroup" testname="{x(name)}" enabled="true">'
        f'{prop("stringProp", "TestPlan.comments", comment)}'
        f'{prop("stringProp", "ThreadGroup.on_sample_error", "continue")}'
        '<elementProp name="ThreadGroup.main_controller" elementType="LoopController" guiclass="LoopControlPanel" '
        'testclass="LoopController" testname="Loop Controller" enabled="true">'
        f'{prop("boolProp", "LoopController.continue_forever", forever)}{prop("stringProp", "LoopController.loops", loops)}'
        '</elementProp>'
        f'{prop("stringProp", "ThreadGroup.num_threads", threads)}{prop("stringProp", "ThreadGroup.ramp_time", ramp)}'
        f'{prop("boolProp", "ThreadGroup.scheduler", "true" if duration else "false")}'
        f'{prop("stringProp", "ThreadGroup.duration", duration or "")}{prop("stringProp", "ThreadGroup.delay", "")}'
        '</ThreadGroup>', children)


def sampler(name, method, path, comment="", form=(), body=None, multipart=False, children=()):
    args = arguments(form, http=True)
    if body is not None:
        args = (f'<elementProp name="" elementType="HTTPArgument">{prop("boolProp", "HTTPArgument.always_encode", "false")}'
                f'{prop("stringProp", "Argument.value", body)}{prop("stringProp", "Argument.metadata", "=")}</elementProp>')
    if multipart and not form:
        args = arguments([("placeholder", "x")], http=True)
    return node(
        f'<HTTPSamplerProxy guiclass="HttpTestSampleGui" testclass="HTTPSamplerProxy" testname="{x(name)}" enabled="true">'
        f'{prop("stringProp", "TestPlan.comments", comment)}'
        + (prop("boolProp", "HTTPSampler.postBodyRaw", "true") if body is not None else "")
        + '<elementProp name="HTTPsampler.Arguments" elementType="Arguments" guiclass="HTTPArgumentsPanel" '
        f'testclass="Arguments" enabled="true"><collectionProp name="Arguments.arguments">{args}</collectionProp></elementProp>'
        f'{prop("stringProp", "HTTPSampler.path", path)}{prop("stringProp", "HTTPSampler.method", method)}'
        f'{prop("boolProp", "HTTPSampler.follow_redirects", "false")}{prop("boolProp", "HTTPSampler.auto_redirects", "false")}'
        f'{prop("boolProp", "HTTPSampler.use_keepalive", "true")}{prop("boolProp", "HTTPSampler.DO_MULTIPART_POST", "true" if multipart else "false")}'
        '</HTTPSamplerProxy>', children)


def headers(pairs, name="Headers"):
    items = "".join(f'<elementProp name="" elementType="Header">{prop("stringProp", "Header.name", k)}'
                    f'{prop("stringProp", "Header.value", v)}</elementProp>' for k, v in pairs)
    return node(f'<HeaderManager guiclass="HeaderPanel" testclass="HeaderManager" testname="{x(name)}" enabled="true">'
                f'<collectionProp name="HeaderManager.headers">{items}</collectionProp></HeaderManager>')


def status_assertion(name, pattern, negate=False, ignore_status=True):
    # Assertion.test_type: MATCHES=1, NOT=4. ignore_status (assume_success) lets an expected 4xx pass;
    # JMeter resets the sample to successful before such an assertion runs, so on a sample with
    # several assertions only the first may ignore the status.
    return node(f'<ResponseAssertion guiclass="AssertionGui" testclass="ResponseAssertion" testname="{x(name)}" enabled="true">'
                f'<collectionProp name="Asserion.test_strings"><stringProp name="0">{x(pattern)}</stringProp></collectionProp>'
                f'{prop("stringProp", "Assertion.test_field", "Assertion.response_code")}'
                f'{prop("boolProp", "Assertion.assume_success", "true" if ignore_status else "false")}'
                f'{prop("intProp", "Assertion.test_type", "5" if negate else "1")}</ResponseAssertion>')


def allowed_assertions():
    """An HTTP response (so an unreachable API fails) that is not 401, 403 or 429."""
    return [status_assertion("HTTP response", "[1-5][0-9][0-9]"),
            status_assertion("authorized (not 401/403/429)", "401|403|429", negate=True, ignore_status=False)]


def token_extractor():
    return node('<RegexExtractor guiclass="RegexExtractorGui" testclass="RegexExtractor" testname="access_token" enabled="true">'
                f'{prop("stringProp", "RegexExtractor.useHeaders", "false")}{prop("stringProp", "RegexExtractor.refname", "access_token")}'
                f'{prop("stringProp", "RegexExtractor.regex", '"access_token"\\s*:\\s*"([^"]+)"')}'
                f'{prop("stringProp", "RegexExtractor.template", "$1$")}{prop("stringProp", "RegexExtractor.default", "NO_TOKEN")}'
                f'{prop("stringProp", "RegexExtractor.match_number", "1")}</RegexExtractor>')


def login(role, client):
    return sampler(
        f"Sign in as {g.seed_username(role)}", "POST",
        "${keycloakUrl}/realms/${realm}/protocol/openid-connect/token",
        comment=f"Password grant on {client} (test realm)",
        form=[("grant_type", "password"), ("client_id", client),
              ("username", g.seed_username(role)), ("password", "${password}")],
        children=[token_extractor(), status_assertion("signed in", "200")])


def simple_controller(name, children):
    return node(f'<GenericController guiclass="LogicControllerGui" testclass="GenericController" testname="{x(name)}" enabled="true"/>',
                children)


def once_only(name, children):
    return node(f'<OnceOnlyController guiclass="OnceOnlyControllerGui" testclass="OnceOnlyController" testname="{x(name)}" enabled="true"/>',
                children)


def constant_timer(ms):
    return node(f'<ConstantTimer guiclass="ConstantTimerGui" testclass="ConstantTimer" testname="Pace" enabled="true">'
                f'{prop("stringProp", "ConstantTimer.delay", ms)}</ConstantTimer>')


def think_time():
    return node('<UniformRandomTimer guiclass="UniformRandomTimerGui" testclass="UniformRandomTimer" testname="Think time" enabled="true">'
                f'{prop("stringProp", "ConstantTimer.delay", "${__P(thinkMin,500)}")}'
                f'{prop("stringProp", "RandomTimer.range", "${__javaScript(${__P(thinkMax,2000)}-${__P(thinkMin,500)})}")}'
                '</UniformRandomTimer>')


def client_for(role):
    tree = g.effective_roles(role)
    for client, (_, base, _) in g.PORTAL_ROLES.items():
        if base == "STAFF" and "STAFF" in tree:
            return client
    return next(c for c, (_, base, _) in g.PORTAL_ROLES.items() if base == "CUSTOMER")


def variables(extra=()):
    return [(name, f"${{__P({name},{default})}}") for name, default, _ in PROPERTIES + list(extra)]


# --------------------------------------------------------------------------- plans
def authorization_plan(endpoints, granted, enums):
    groups, stats = [], defaultdict(int)

    # Public endpoints and missing tokens. Anonymous calls are rate limited per IP
    # (rate-limiting.public-tokens, 30/min), so they are paced and one per resource is checked.
    anonymous = []
    for e in endpoints:
        if e["scope"] == "public":
            path, body, _ = request_for(e, enums)
            anonymous.append(sampler(f"public {e['http']} {e['path']}", e["http"], path,
                                     comment="Public endpoint: reachable without a token", body=body,
                                     children=allowed_assertions()))
            stats["public"] += 1
    seen = set()
    for e in endpoints:
        if e["scope"] not in ("public",) and e["http"] == "GET" and e["resource"] not in seen:
            seen.add(e["resource"])
            path, _, _ = request_for(e, enums)
            anonymous.append(sampler(f"no token {e['http']} {e['path']}", "GET", path,
                                     comment="Protected endpoint called without a token",
                                     children=[status_assertion("401", "401")]))
            stats["no token"] += 1
    anonymous.append(sampler("GET /actuator/health", "GET", "/actuator/health",
                             children=[status_assertion("200", "200")]))
    anonymous.append(sampler("GET /actuator/env (not exposed)", "GET", "/actuator/env",
                             children=[status_assertion("not served", "401|403|404")]))
    groups.append(thread_group("Anonymous", [constant_timer("${__P(anonymousPaceMs,2100)}")] + anonymous,
                               comment="Public endpoints reachable, protected ones 401, actuator locked down"))

    for role in g.SAMPLE_USER_ROLES:
        calls = []
        for e in endpoints:
            if e["scope"] == "public":
                continue
            outcome, reason = expectation(role, e, granted)
            if outcome == "allow" and e["http"] != "GET":
                stats["skipped writes"] += 1
                continue  # never change data
            path, body, multipart = request_for(e, enums)
            label = f"{'ALLOW' if outcome == 'allow' else 'DENY'} {e['http']} {e['path']}"
            if outcome == "allow":
                children = allowed_assertions()
            elif e["http"] == "GET":
                children = [status_assertion("403", "403")]
            else:
                # A body or multipart part can be rejected (400/415) before authorization runs
                children = [status_assertion("refused (403, or 400/415 before authorization)", "403|400|415")]
            if body is not None:
                children.insert(0, headers([("Content-Type", "application/json")], "JSON body"))
            calls.append(sampler(label, e["http"], path, comment=f"{e['label']}: {reason}",
                                 body=body, multipart=multipart, children=children))
            stats[outcome] += 1
        groups.append(thread_group(
            f"Role {role}", [login(role, client_for(role)),
                             simple_controller("Endpoints", [headers([("Authorization", "Bearer ${access_token}")],
                                                                     "Bearer token")] + calls)],
            comment=f"{g.ROLES[role]}. Expected outcomes from ROLE_MATRIX.md and the @PreAuthorize expressions."))

    comment = ("Authorization of every endpoint for every sample user (test realm). ALLOW = the role holds the "
               "permission (only GET is called); DENY = 403. Generated by scripts/generate_jmeter.py.")
    return test_plan("knowvera API authorization", comment,
                     variables([("anonymousPaceMs", "2100", "Delay between anonymous calls (public rate limit)")]),
                     [http_defaults()] + groups), stats


def load_plan(endpoints, enums):
    by_label = {e["label"]: e for e in endpoints}
    defaults = {name: default for name, default, _ in LOAD_PROPERTIES}

    def get(label, name=None):
        e = by_label.get(label)
        if e is None:
            sys.exit(f"generate_jmeter.py: the load journeys use {label}, which no longer exists")
        path, _, _ = request_for(e, enums)
        # Any 2xx: empty lists may answer 204 No Content
        return sampler(name or f"{e['http']} {e['path']}", "GET", path, comment=label,
                       children=[status_assertion("2xx", "2[0-9][0-9]")])

    def journey(name, role, threads_prop, steps, comment):
        return thread_group(
            # The bearer header and think time apply to the journey only, not to the sign-in
            name, [once_only("Sign in once", [login(role, client_for(role))]),
                   simple_controller("Journey", [headers([("Authorization", "Bearer ${access_token}")], "Bearer token"),
                                                 think_time()] + steps)],
            threads=f"${{__P({threads_prop},{defaults[threads_prop]})}}", loops="-1",
            ramp=f"${{__P(rampUp,{defaults['rampUp']})}}", duration=f"${{__P(duration,{defaults['duration']})}}",
            comment=comment)

    groups = [
        journey("Back office: KYC analyst", "KYC_ANALYST", "staffThreads", [
            get("KycRecordApi.getAllPaged", "KYC records, page 1"),
            get("DocumentApi.getAllPaged", "Documents, page 1"),
            get("ClientRequestApi.getAllPaged", "Client requests, page 1"),
            get("IndividualApi.getAllPaged", "Individuals, page 1"),
            get("DocumentTypeApi.getAll", "Document types"),
        ], "Works through the case lists"),
        journey("Customer: applicant", "APPLICANT", "applicantThreads", [
            get("IndividualApi.loadMe", "My profile"),
            get("KycRecordApi.findMyRecords", "My KYC records"),
            get("DocumentApi.findMyDocuments", "My documents"),
            get("KycFieldGroupApi.findByTarget", "KYC form fields"),
            get("DocumentTypeApi.getAll", "Document types"),
        ], "Checks their application"),
        journey("Customer: organisation administrator", "ORG_ADMIN", "orgThreads", [
            get("OrganisationApi.loadMyOrganisation", "My organisation"),
            get("ClientRequestApi.findMyOrganisationRequests", "Our client requests"),
            get("KycRecordApi.findMyRecords", "Our KYC records"),
            get("DocumentTypeApi.getAll", "Document types"),
        ], "Follows their organisation's client requests"),
    ]
    comment = ("Read-heavy load journeys; sizes via -J properties (see jmeter/README.md). The sample users are "
               "shared by all threads of a journey, so raise RATE_LIMIT on the API above "
               "(threads x requests per minute). Generated by scripts/generate_jmeter.py.")
    # The journeys run at the same time
    return test_plan("knowvera API load", comment, variables(LOAD_PROPERTIES), [http_defaults()] + groups, serial=False)


def main():
    endpoints, granted = build_endpoints()
    enums = enum_first_constants()
    OUT.mkdir(exist_ok=True)
    authz, stats = authorization_plan(endpoints, granted, enums)
    (OUT / "knowvera-authorization.jmx").write_text(authz)
    (OUT / "knowvera-load.jmx").write_text(load_plan(endpoints, enums))
    print(f"knowvera-authorization.jmx: {len(g.SAMPLE_USER_ROLES)} roles; "
          + ", ".join(f"{k} {v}" for k, v in sorted(stats.items())))
    print("knowvera-load.jmx: 3 journeys")


if __name__ == "__main__":
    main()
