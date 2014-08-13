#!/bin/bash

PROJECT_NAME=$1

if [ -z $PROJECT_NAME ];then
  echo "Please define a module name."
  exit 1
fi

WORK_DIR=/data/compileAnddeploy/testEnv
SRC_DIR=$WORK_DIR/source
RELEASE_DIR=$WORK_DIR/release

if [ "mlw-pay" = "$PROJECT_NAME" -o "mlw-account" = "$PROJECT_NAME" -o "mlw-stock" = "$PROJECT_NAME" ];then
  TO_IP="10.14.16.198 10.14.16.199"
elif [ "mlw-antispam" = "$PROJECT_NAME" -o "mlw-base" = "$PROJECT_NAME" -o "mlw-bkstage" = "$PROJECT_NAME" -o "mlw-cms" = "$PROJECT_NAME" ];then
  TO_IP="10.14.16.198 10.14.16.211"
elif [ "mlw-mms" = "$PROJECT_NAME" -o "mlw-monitor" = "$PROJECT_NAME" -o "mlw-oms" = "$PROJECT_NAME" -o "mlw-pms" = "$PROJECT_NAME" -o "mlw-sp" = "$PROJECT_NAME" ];then
  TO_IP="10.14.16.199 10.14.16.211"
fi

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
    if [ "mlw-stock" == "$PROJECT_NAME" -a $index -eq 1];then
      continue
    fi

    echo "ssh root@$ip /data/iceservice/restartIceBox.sh $PROJECT_NAME $indexName"
    ssh root@$ip "/data/iceservice/restartIceBox.sh $PROJECT_NAME $indexName"
    
    if [ $index -lt 2 ];then
      echo ""
      read -t 300 -p "Do you want to release this version for $1 to next machine?[y/n]" wtn
      if [ "y" == "$wtn" ];then
        continue
      fi
    fi

    read -t 300 -p "Do you want to rollback this version for $1 in $ip?[y/n]" rbk
    if [ -z "$rbk" -o "y" != "$rbk" ];then
      echo "will not rollback this version, program will exit."
      exit
    else
      RBK_IP="$ip"
      break
    fi
  done

if [ "y" == "$rbk" -a ! -z "$RBK_IP" ];then
  echo "begin to rollback this version for $1 in $RBK_IP"
  echo "ssh root@$ip /data/iceservice/rollback.sh $PROJECT_NAME $indexName"
  ssh root@$ip "/data/iceservice/rollback.sh $PROJECT_NAME $indexName"
fi
