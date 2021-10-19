#!/bin/bash

APP_NAME=vocabulary-api;

default_label=$1

if [[ -z $PW ]];
then
    echo 'Spring Cloud Git server label/branch not specified, will use default "develop"';
    default_label="develop";
fi

jasypt=$(cat ./env/jasypt_password);
tspass=$(cat ./env/truststore_password);

echo "Stopping container ${APP_NAME}";
docker stop ${APP_NAME} > /dev/null 2>&1;

echo "Deleting container ${APP_NAME}";
docker rm ${APP_NAME} > /dev/null 2>&1;

docker run -d -p 8080:8080 -e "GIT_BRANCH=$default_label" \
-e "JAVA_TOOL_OPTIONS=-Dspring.profiles.active=prod -Djasypt.encryptor.password=$jasypt -Djavax.net.ssl.trustStore=config/keystore/vocabulary-api.p12 -Djavax.net.ssl.trustStorePassword=$tspass" \
--name ${APP_NAME} ${APP_NAME}:latest;