#!/bin/bash

cd `dirname $0`

RELEASE_DIR=/data/compileAnddeploy/mlw/release

RELEASE_FILE=`find $RELEASE_DIR -type f | grep tar.gz | grep $1`

if [ -z $RELEASE_FILE ]; then
  echo "not found ${1}*.war in $RELEASE_DIR, then exit."
  exit
fi

echo "find release jars $RELEASE_FILE for project $1"

cp $RELEASE_FILE /tmp/${1}.tar.gz

echo "/data/scripts/redeploy.sh"
/data/scripts/redeploy.sh

