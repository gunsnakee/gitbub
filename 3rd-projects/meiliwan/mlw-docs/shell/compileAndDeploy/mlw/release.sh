#!/bin/bash

PROJECT_NAME=$1

if [ -z $PROJECT_NAME ];then
  echo "Please define a module name."
  exit 1
fi

cd `dirname $0`

TO_IP="10.249.15.194"
WORK_DIR=/data/compileAnddeploy/mlw
SRC_DIR=$WORK_DIR/source
RELEASE_DIR=$WORK_DIR/release

RELEASE_FILES=`find $RELEASE_DIR -name "${PROJECT_NAME}-service*SNAPSHOT.jar"`

if [ -z "$RELEASE_FILES" ]; then
  echo "not found file with pattern ${PROJECT_NAME}-service*SNAPSHOT.jar  in $RELEASE_DIR, then exit."
  exit
fi

echo "find release jars $RELEASE_FILES for project $PROJECT_NAME"

if [ "mlw-gateway" == "$PROJECT_NAME" ];then
  PROJECT_NAME="mlw-pay"
elif [ "mlw-passport" == "$PROJECT_NAME" ];then
  PROJECT_NAME="mlw-mms"
elif [ "mlw-wallet" == "$PROJECT_NAME" ];then
  PROJECT_NAME="mlw-account"
elif [ "mlw-admin" == "$PROJECT_NAME" ];then
  PROJECT_NAME="mlw-bkstage"
fi

index=0
indexName=""
for ip in $TO_IP
  do 
    echo "ssh root@$ip /data/iceservice/backup.sh $PROJECT_NAME"
    ssh root@$ip "/data/iceservice/backup.sh $PROJECT_NAME"

    echo "find $RELEASE_DIR -name mlw-commons*SNAPSHOT.jar"
    COMMON_JAR=`find $RELEASE_DIR -name "mlw-commons*SNAPSHOT.jar"`
    if [ ! -z "$COMMON_JAR" ];then
      echo "scp $COMMON_JAR root@$ip:/data/iceservice/node/lib/$PROJECT_NAME"
      scp $COMMON_JAR root@$ip:/data/iceservice/node/lib/$PROJECT_NAME
    fi

    echo "find $RELEASE_DIR -name mlw*-client*SNAPSHOT.jar" 
    CLIENT_JAR=`find $RELEASE_DIR -name "mlw*-client*SNAPSHOT.jar"`
    if [ ! -z "$CLIENT_JAR" ];then
      echo "scp $CLIENT_JAR root@$ip:/data/iceservice/node/lib/$PROJECT_NAME"
      scp $CLIENT_JAR root@$ip:/data/iceservice/node/lib/$PROJECT_NAME
    fi

    echo "scp $RELEASE_FILES root@$ip:/data/iceservice/node/lib/$PROJECT_NAME"
    scp $RELEASE_FILES root@$ip:/data/iceservice/node/lib/$PROJECT_NAME

    let index+=1
    if [ $index -gt 1  ];then
      indexName=$index
    fi
    echo "ssh root@$ip /data/iceservice/restartIceBox.sh $PROJECT_NAME $indexName"
    ssh root@$ip "/data/iceservice/restartIceBox.sh $PROJECT_NAME $indexName"
  done

