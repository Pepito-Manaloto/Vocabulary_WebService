#!/bin/bash

PROJECT_NAME=vocab-dc;

docker-compose -p $PROJECT_NAME down --rmi local;

docker-compose -p $PROJECT_NAME up --build --force-recreate -d