#!/bin/bash

exit_if_last_command_failed()
{
  if [[ "$?" -ne 0 ]];
  then
    echo $1;
    exit 1;
  fi
}

mvn clean package;

exit_if_last_command_failed 'Maven build failed. Exiting...';

APP_NAME=vocabulary-api;

echo "Stopping container ${APP_NAME}";
docker stop ${APP_NAME} > /dev/null 2>&1;

echo "Deleting container ${APP_NAME}";
docker rm ${APP_NAME} > /dev/null 2>&1;

IMAGE_ID=$(docker images --filter=reference=${APP_NAME} --format "{{.ID}}");
echo "Deleting image ${IMAGE_ID} of ${APP_NAME}";
docker rmi ${IMAGE_ID} > /dev/null 2>&1;

echo "Building new image ${APP_NAME}:latest";
docker build -t ${APP_NAME}:latest .;

echo "Done";
