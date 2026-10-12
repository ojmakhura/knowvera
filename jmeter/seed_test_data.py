#!/usr/bin/env python3
"""
Test data the load journeys need, created through the API the way staff would (idempotent):

  - applicant  gets an individual profile (created by kyc.analyst)
  - org.admin  is linked to the "JMeter Test Organisation" (created by case.manager if missing,
               linked by platform.admin)

Run through `jmeter/test-stack.sh seed` once the test API is up.
"""

import base64
import json
import ssl
import sys
import urllib.error
import urllib.parse
import urllib.request

API, KEYCLOAK, REALM, PASSWORD = sys.argv[1:5]
ORG = {
    "name": "JMeter Test Organisation",
    "code": "JMTORG",
    "registrationNo": "JMT-ORG-001",
    "countryOfRegistration": "Botswana",
    "status": "ACTIVE",
    "kycStatus": "INCOMPLETE",
    "domains": [{"name": "jmeter.knowvera.test"}],
    "isClient": True,  # client organisations also exist as Keycloak Organizations
}
PROFILE = {
    "firstName": "Ann", "surname": "Applicant", "identityNo": "JMT0000001", "identityType": "OMANG",
    "kycStatus": "INCOMPLETE", "sex": "FEMALE", "maritalStatus": "SINGLE", "employmentStatus": "EMPLOYED",
    "nationality": "Motswana", "emailAddress": "applicant@knowvera.test",
}
# The local Keycloak sits behind a proxy with a self-signed certificate
TLS = ssl._create_unverified_context()


def call(method, url, token=None, body=None, form=None):
    data, headers = None, {}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    if body is not None:
        data, headers["Content-Type"] = json.dumps(body).encode(), "application/json"
    if form is not None:
        data, headers["Content-Type"] = urllib.parse.urlencode(form).encode(), "application/x-www-form-urlencoded"
    request = urllib.request.Request(url, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(request, context=TLS if url.startswith("https") else None) as response:
            text = response.read().decode()
            return response.status, json.loads(text) if text.strip() else None
    except urllib.error.HTTPError as e:
        text = e.read().decode()
        return e.code, json.loads(text) if text.strip().startswith(("{", "[")) else text


def sign_in(username, client):
    status, body = call("POST", f"{KEYCLOAK}/realms/{REALM}/protocol/openid-connect/token",
                        form={"grant_type": "password", "client_id": client, "username": username, "password": PASSWORD})
    if status != 200:
        sys.exit(f"Sign-in as {username} failed: {status} {body}")
    token = body["access_token"]
    subject = json.loads(base64.urlsafe_b64decode(token.split(".")[1] + "=="))["sub"]
    return token, subject


def check(status, body, what):
    if status >= 300:
        sys.exit(f"{what} failed: {status} {body}")
    return body


def seed_applicant():
    token, subject = sign_in("applicant", "knowvera-web")
    if call("GET", f"{API}/individuals/me", token)[0] == 200:
        print("applicant: profile already present")
        return
    analyst, _ = sign_in("kyc.analyst", "admin-portal")
    created = check(*call("POST", f"{API}/individuals", analyst, {**PROFILE, "userId": subject}), "Creating the profile")
    print(f"applicant: profile {created['id']} created")


def seed_org_admin():
    token, subject = sign_in("org.admin", "knowvera-web")
    if call("GET", f"{API}/organisations/mine", token)[0] == 200:
        print("org.admin: organisation already linked")
        return

    manager, _ = sign_in("case.manager", "admin-portal")
    status, organisation = call("GET", f"{API}/organisations/registration/{ORG['registrationNo']}", manager)
    if status != 200 or not organisation:
        organisation = check(*call("POST", f"{API}/organisations", manager, ORG), "Creating the organisation")
        print(f"organisation {organisation['id']} created")
    elif not organisation.get("isClient"):
        organisation = check(*call("POST", f"{API}/organisations", manager, {**organisation, "isClient": True}),
                             "Making the organisation a client")
        print(f"organisation {organisation['id']} made a client organisation")

    admin, _ = sign_in("platform.admin", "admin-portal")
    account = check(*call("GET", f"{API}/users?userId={subject}", admin), "Loading org.admin's account")
    account["organisationId"] = organisation["id"]
    check(*call("POST", f"{API}/users/create", admin, account), "Linking org.admin")

    status, linked = call("GET", f"{API}/organisations/mine", token)
    check(status, linked, "Checking the link")
    print(f"org.admin: linked to {linked['name']} ({linked['id']})")


if __name__ == "__main__":
    seed_applicant()
    seed_org_admin()
