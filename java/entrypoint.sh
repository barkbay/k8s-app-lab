#!/bin/bash

# DO NOT CALL 'java' directly -- USE 'exec'
exec java ${JAVA_OPTS} -jar /app.jar
