#!/bin/bash

SERVICE_NAME=$1
PROJECT_NAME=mlw-mq-job
RELEASE_DIR=/data/compileAnddeploy/testEnv/release
SRC_DIR=/data/compileAnddeploy/testEnv/source
APP_WORK_DIR=/data/software/mq_app
TO_IP="10.14.16.151 10.14.16.155"

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

index=0
for ip in $TO_IP
  do 
    echo "ssh root@$ip $APP_WORK_DIR/backup.sh $SERVICE_NAME"
    ssh root@$ip $APP_WORK_DIR/backup.sh $SERVICE_NAME

    echo "scp $RELEASE_FILES $SRC_DIR/$PROJECT_NAME/target/lib/* root@$ip:/data/software/mq_app/$SERVICE_NAME/lib/"
    scp $RELEASE_FILES $SRC_DIR/$PROJECT_NAME/target/lib/* root@$ip:/data/software/mq_app/$SERVICE_NAME/lib/

    let index+=1
    if [ $index -gt 1  ];then
      echo "$ip do not need restart, then exit"
      break;
    fi

    echo ""
    echo "ssh root@$ip $APP_WORK_DIR/service_check.sh $SERVICE_NAME -k"
    ssh root@$ip $APP_WORK_DIR/service_check.sh $SERVICE_NAME -k

    read -t 300 -p "Do you want to rollback?[y/n]" rbk
    if [ -z "$rbk" -o "y" != "$rbk" ];then
      echo "will not rollback this version, program will exit."
      exit
    else
      RBK_IP="$ip"
      break
    fi
done

if [ "y" == "$rbk" -a ! -z "$RBK_IP" ];then
  echo "begin to rollback this version for ip $RBK_IP"
  echo "ssh root@$ip $APP_WORK_DIR/rollback.sh $PROJECT_NAME"
  #ssh root@$ip "$APP_WORK_DIR/rollback.sh $PROJECT_NAME"
fi
