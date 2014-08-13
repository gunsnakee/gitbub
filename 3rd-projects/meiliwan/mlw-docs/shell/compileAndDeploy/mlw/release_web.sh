#!/bin/bash

cd `dirname $0`

RELEASE_DIR=/data/compileAnddeploy/mlw/release

RELEASE_FILE=`find $RELEASE_DIR -type f | grep war | grep $1`

if [ -z $RELEASE_FILE ]; then
  echo "not found ${1}*.war in $RELEASE_DIR, then exit."
  exit
fi

echo "find release jars $RELEASE_FILE for project $1"

cp $RELEASE_FILE /tmp/${1}.war

DOMAIN=${1#mlw-}
DOMAIN=${DOMAIN%%-*}

if [ "wallet" == "$DOMAIN" ];then
  DOMAIN="account"
fi

echo "/data/scripts/restart.sh $DOMAIN"
/data/scripts/restart.sh $DOMAIN

