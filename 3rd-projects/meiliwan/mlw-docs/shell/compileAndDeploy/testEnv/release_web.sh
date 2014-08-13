#!/bin/bash

cd `dirname $0`

TO_IP="10.249.15.199 10.249.15.200"
RELEASE_DIR=/data/compileAnddeploy/testEnv/release

RELEASE_FILE=`find $RELEASE_DIR -type f | grep war | grep $1`

if [ -z $RELEASE_FILE ]; then
  echo "not found ${1}*.war in $RELEASE_DIR, then exit."
  exit
fi

echo "find release jars $RELEASE_FILE for project $1"


DOMAIN=${1#mlw-}
DOMAIN=${DOMAIN%%-*}

if [ "wallet" == "$DOMAIN" ];then
  DOMAIN="account"
fi

index=0
for ip in $TO_IP
  do
    echo "scp $RELEASE_FILE root@$ip:/tmp/${1}.war"
    scp $RELEASE_FILE root@$ip:/tmp/${1}.war

    echo "ssh root@$ip '/data/scripts/restart.sh $DOMAIN'"
    ssh root@$ip "/data/scripts/restart.sh $DOMAIN"

    let index+=1
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
  echo "ssh root@$ip /data/scripts/rollback.sh $DOMAIN"
  ssh root@$ip "/data/scripts/rollback.sh $DOMAIN"
fi

