#!/bin/bash

SERVICE_NAME=$1
PROJECT_NAME=mlw-mq-job
RELEASE_DIR=/data/compileAnddeploy/mlw/release
SRC_DIR=/data/compileAnddeploy/mlw/source
APP_WORK_DIR=/data/software/mq_app

if [ -z $SERVICE_NAME ];then
  echo "Please define an async module name like img_service log_service ord_service."
  exit 1
fi

RELEASE_FILES=`find $RELEASE_DIR -name "${PROJECT_NAME}*SNAPSHOT.jar"`

if [ -z "$RELEASE_FILES" ]; then
  echo "not found file with pattern ${PROJECT_NAME}*SNAPSHOT.jar  in $RELEASE_DIR, then exit."
  exit
fi

echo "find release jars $RELEASE_FILES for project $PROJECT_NAME"

$APP_WORK_DIR/backup.sh $SERVICE_NAME
cp $RELEASE_FILES $APP_WORK_DIR/$SERVICE_NAME/lib
cp $SRC_DIR/$PROJECT_NAME/target/lib/* $APP_WORK_DIR/$SERVICE_NAME/lib

echo ""
$APP_WORK_DIR/service_check.sh $SERVICE_NAME -k
