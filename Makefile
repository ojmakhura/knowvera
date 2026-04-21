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
	. ./.env && cd angular/target/centralkyc && npm run build --configuration=production

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
	. ./.env && docker compose build api

###
## tag and push the images
###
push_web_image: 
	. ./.env && docker push ${REGISTRY_TAG}/${WEB_IMAGE_NAME}:${IMAGE_VERSION}${IMAGE_VERSION_SUFFIX}

push_api_image: 
	. ./.env && docker push ${REGISTRY_TAG}/${API_IMAGE_NAME}:${IMAGE_VERSION}${IMAGE_VERSION_SUFFIX}


###
## Run the local api and web
###    
run_module_local:
	. ./.env && cd ${module} && mvn spring-boot:run
	
run_api_local: 
	. ./.env && mvn -pl webservice/ -am spring-boot:run

local_web_deps: build_web
	cd angular/target/centralkyc && npm i

local_web_deps_force:
	cd angular/target/centralkyc && npm i --force

run_web_local: build_web
	cd angular/target/centralkyc && npm start

update_portal_deps:
	cp -rf angular/target/src/app/models/* portal/src/app/models/
	cp -rf angular/src/app/models/* portal/src/app/models/
	cp -rf angular/src/app/services/* portal/src/app/services/
	cp -rf angular/src/app/store/* portal/src/app/store/