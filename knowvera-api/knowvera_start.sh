#!/usr/bin/env sh
# exec: the JVM is the container's main process and receives its stop signal.
# The heap follows the container's memory limit; JAVA_OPTS adds to or overrides this.
exec java -XX:MaxRAMPercentage=75 ${JAVA_OPTS} -jar /app/knowvera-webservice.jar
