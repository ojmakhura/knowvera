# include ./Makefile.dev

###
## knowvera-api: common <- core <- webservice (knowvera-api/pom.xml is the top-level build)
###
API := knowvera-api
MVN_API := mvn -f $(API)/pom.xml
API_VERSION = $$($(MVN_API) help:evaluate -Dexpression=project.version -q -DforceStdout)

build_common: build_veritas_common
	$(MVN_API) -pl common install -DskipTests=true

build_core: build_veritas_common
	$(MVN_API) -pl core -am install -DskipTests=true

build_api: build_veritas_common
	. ./.env && $(MVN_API) install -DskipTests=true

test_core: build_veritas_common
	. ./.env && $(MVN_API) -pl core -am test

test_webservice: build_core
	. ./.env && $(MVN_API) -pl webservice test

test_api: build_veritas_common
	. ./.env && $(MVN_API) test

clean_api:
	$(MVN_API) clean

###
## veritas-processor: document AI processing (common <- core <- webservice); knowvera-api depends on its common module
###
VERITAS := veritas-processor
MVN_VERITAS := mvn -f $(VERITAS)/pom.xml
VERITAS_VERSION = $$($(MVN_VERITAS) help:evaluate -Dexpression=project.version -q -DforceStdout)

build_veritas:
	$(MVN_VERITAS) install -DskipTests=true

test_veritas:
	$(MVN_VERITAS) test

clean_veritas:
	$(MVN_VERITAS) clean

run_veritas_local: build_veritas
	. ./.env && $(MVN_VERITAS) -pl webservice spring-boot:run

build_veritas_image: build_veritas
	. ./.env && export VERITAS_IMAGE_VERSION=$(VERITAS_VERSION) && docker build -t $${VERITAS_IMAGE_NAME:-veritas-processor}:$${VERITAS_IMAGE_VERSION} $(VERITAS) && docker tag $${VERITAS_IMAGE_NAME:-veritas-processor}:$${VERITAS_IMAGE_VERSION} $${VERITAS_IMAGE_NAME:-veritas-processor}:latest

push_veritas_image:
	. ./.env && export VERITAS_IMAGE_VERSION=$(VERITAS_VERSION) && docker push $${VERITAS_IMAGE_NAME:-veritas-processor}:$${VERITAS_IMAGE_VERSION} && docker push $${VERITAS_IMAGE_NAME:-veritas-processor}:latest
	
build_portal_dist: 
	. ./.env && cd portal && npm run build --configuration=production

build_admin_portal_dist: 
	. ./.env && cd admin-portal && npm run build --configuration=production

build_native: clean_api build_core
	. ./.env && $(MVN_API) -Pnative -pl webservice native:compile -DskipTests

native_image: build_core
	. ./.env && $(MVN_API) -Pnative -pl webservice spring-boot:build-image -DskipTests

build_app: build_veritas build_api

# The job/profile contracts knowvera-api compiles against
build_veritas_common:
	mvn -f veritas-processor/pom.xml -pl common install -DskipTests=true

clean_build: clean_all build_app

clean_all: clean_api clean_veritas

clean_module:
	mvn -f ${module} clean
	
build_api_image: build_api
	. ./.env && export API_IMAGE_VERSION=$(API_VERSION) && docker build -t $${API_IMAGE_NAME}:$${API_IMAGE_VERSION} $(API) && docker tag $${API_IMAGE_NAME}:$${API_IMAGE_VERSION} $${API_IMAGE_NAME}:latest

build_portal_image: build_portal_dist
	. ./.env && export PORTAL_IMAGE_VERSION=$$(node -p "require('./portal/package.json').version") && docker build -t $${PORTAL_IMAGE_NAME}:$${PORTAL_IMAGE_VERSION} portal && docker tag $${PORTAL_IMAGE_NAME}:$${PORTAL_IMAGE_VERSION} $${PORTAL_IMAGE_NAME}:latest

build_admin_image: build_admin_portal_dist
	. ./.env && export ADMIN_IMAGE_VERSION=$$(node -p "require('./admin-portal/package.json').version") && docker build -t $${ADMIN_IMAGE_NAME}:$${ADMIN_IMAGE_VERSION} admin-portal && docker tag $${ADMIN_IMAGE_NAME}:$${ADMIN_IMAGE_VERSION} $${ADMIN_IMAGE_NAME}:latest
###
## tag and push the images
###
push_portal_image: 
	. ./.env && export PORTAL_IMAGE_VERSION=$$(node -p "require('./portal/package.json').version") && docker push $${PORTAL_IMAGE_NAME}:$${PORTAL_IMAGE_VERSION} && docker push $${PORTAL_IMAGE_NAME}:latest

push_admin_image: 
	. ./.env && export ADMIN_IMAGE_VERSION=$$(node -p "require('./admin-portal/package.json').version") && docker push $${ADMIN_IMAGE_NAME}:$${ADMIN_IMAGE_VERSION} && docker push $${ADMIN_IMAGE_NAME}:latest

push_api_image: 
	. ./.env && export API_IMAGE_VERSION=$(API_VERSION) && docker push $${API_IMAGE_NAME}:$${API_IMAGE_VERSION} && docker push $${API_IMAGE_NAME}:latest


###
## Versions
## make versions                         -> show current versions
## make set_api_version VERSION=1.2.0    -> knowvera-api (all its maven modules)
## make set_portal_version VERSION=1.2.0
## make set_admin_version VERSION=1.2.0
## make set_versions VERSION=1.2.0       -> backend + both portals
###
check_version:
	@test -n "$(VERSION)" || (echo "VERSION is required, e.g. make $(MAKECMDGOALS) VERSION=1.2.0" && exit 1)

versions:
	@echo "api:          $(API_VERSION)"
	@echo "veritas:      $(VERITAS_VERSION)"
	@echo "portal:       $$(node -p "require('./portal/package.json').version")"
	@echo "admin-portal: $$(node -p "require('./admin-portal/package.json').version")"

set_api_version: check_version
	$(MVN_API) versions:set -DnewVersion=$(VERSION) -DprocessAllModules=true -DgenerateBackupPoms=false

set_portal_version: check_version
	cd portal && npm version $(VERSION) --no-git-tag-version --allow-same-version

set_admin_version: check_version
	cd admin-portal && npm version $(VERSION) --no-git-tag-version --allow-same-version

set_versions: set_api_version set_portal_version set_admin_version

###
## Run the local api and web
###
run_module_local:
	. ./.env && cd ${module} && mvn spring-boot:run
	
run_api_local: build_core
	. ./.env && $(MVN_API) -pl webservice spring-boot:run

run_portal_local:
	cd portal && npm start

run_admin_portal_local:
	cd admin-portal && npm start

# --- JMeter (plans generated by scripts/generate_jmeter.py; see jmeter/README.md) ---
# Override per run, e.g. make jmeter_authz JMETER_ARGS="-JkeycloakUrl=https://keycloak.local -Jhost=api.local -Jport=443 -Jprotocol=https"
JMETER ?= /usr/local/apache-jmeter-5.6.3/bin/jmeter
# Not JMETER_OPTS: the jmeter launcher reads that name as JVM options
JMETER_ARGS ?=

jmeter_authz:
	rm -rf results/authorization results/authorization.jtl
	$(JMETER) -n -t jmeter/knowvera-authorization.jmx -l results/authorization.jtl -e -o results/authorization $(JMETER_ARGS)

jmeter_load:
	rm -rf results/load results/load.jtl
	$(JMETER) -n -t jmeter/knowvera-load.jmx -l results/load.jtl -e -o results/load $(JMETER_ARGS)
