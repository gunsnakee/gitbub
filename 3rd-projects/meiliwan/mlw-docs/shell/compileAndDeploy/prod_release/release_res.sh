#!/bin/bash

TO_IP="10.14.16.146 10.14.16.171 10.14.116.249 10.14.118.149"
RELEASE_DIR=/data/compileAnddeploy/testEnv/release

RELEASE_FILE=`find $RELEASE_DIR -type f | grep tar.gz | grep $1`

if [ -z $RELEASE_FILE ]; then
  echo "not found ${1}*.tar.gz in $RELEASE_DIR, then exit."
  exit
fi

echo "find release jars $RELEASE_FILE for project $1"

for ip in $TO_IP
  do
    echo "scp $RELEASE_FILE root@$ip:/tmp/${1}.tar.gz"
    scp $RELEASE_FILE root@$ip:/tmp/${1}.tar.gz

    echo "ssh root@$ip /data/scripts/redeploy.sh"
    ssh root@$ip /data/scripts/redeploy.sh
  done

