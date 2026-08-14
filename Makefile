# include ./Makefile.dev

build_common:
	mvn -rf common install -DskipTests=true

build_core:
	mvn -rf core install -DskipTests=true

test_core: 
	. ./.env && mvn -pl core test

test_api: 
	. ./.env && mvn -pl webservice test
	
build_api:
	. ./.env && mvn -rf webservice install -DskipTests=true
	
build_mda:
	. ./.env && mvn -rf mda install -DskipTests=true

build_web: 
	mvn -f angular install -DskipTests=true -o

build_web_dist: build_web local_web_deps
	. ./.env && cd angular/target/knowvera && npm run build --configuration=production

build_portal_dist: 
	. ./.env && cd portal && npm run build --configuration=production

build_admin_portal_dist: 
	. ./.env && cd admin-portal && npm run build --configuration=production

build_native: 
	. ./.env && mvn clean && mvn -pl webservice native:compile -Pnative -DskipTests

native_image: 
	. ./.env && mvn -Pnative -pl webservice/ -am spring-boot:build-image

build_app: 
	mvn install -DskipTests=true

clean_build: clean_all build_app

clean_all:
	mvn clean

clean_module:
	mvn -f ${module} clean
	
build_api_image: build_api
	. ./.env && export API_IMAGE_VERSION=$$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && docker build -t $${API_IMAGE_NAME}:$${API_IMAGE_VERSION} . && docker tag $${API_IMAGE_NAME}:$${API_IMAGE_VERSION} $${API_IMAGE_NAME}:latest

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
	. ./.env && export API_IMAGE_VERSION=$$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && docker push $${API_IMAGE_NAME}:$${API_IMAGE_VERSION} && docker push $${API_IMAGE_NAME}:latest


###
## Run the local api and web
###    
run_module_local:
	. ./.env && cd ${module} && mvn spring-boot:run
	
run_api_local: 
	. ./.env && mvn -pl webservice/ -am spring-boot:run

local_web_deps: build_web
	cd angular/target/knowvera && npm i

local_web_deps_force:
	cd angular/target/knowvera && npm i --force

run_web_local: build_web
	cd angular/target/knowvera && npm start

run_portal_local: update_portal_deps
	cd portal && npm start

run_admin_portal_local: update_admin_deps
	cd admin-portal && npm start

update_portal_deps:
	cp -rf angular/target/src/app/models/* portal/src/app/models/
	cp -rf angular/src/app/models/* portal/src/app/models/
	cp -rf angular/src/app/services/* portal/src/app/services/
	cp -rf angular/src/app/store/* portal/src/app/store/

update_admin_deps:
	cp -rf angular/target/src/app/models/* admin-portal/src/app/models/
	cp -rf angular/src/app/models/* admin-portal/src/app/models/
	cp -rf angular/src/app/services/* admin-portal/src/app/services/
	cp -rf angular/src/app/store/* admin-portal/src/app/store/