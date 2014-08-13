#!/bin/bash

PROJECT_NAME=$1
if [ -z $PROJECT_NAME ];then
  echo "Please give a project name, available project name are follow:"
  #echo "  mlw-commons-db mlw-commons-ice mlw-commons-util"
  #echo "  mlw-gateway mlw-oms mlw-passport mlw-pms mlw-sp mlw-stock mlw-wallet"
  #echo "  mlw-admin mlw-antispam mlw-base mlw-cms mlw-monitor mlw-mq-job"
  echo "  mlw-admin-web mlw-cms-web mlw-gateway-web mlw-imagecode-web mlw-monitor-web mlw-passport-web mlw-wallet-web mlw-www-web mlw-web-res"
  exit 1
fi

WORK_DIR=/data/compileAnddeploy/testEnv
RELEASE_DIR=$WORK_DIR/release
BAK_DIR=$WORK_DIR/bak/$PROJECT_NAME
SRC_DIR=$WORK_DIR/source/$PROJECT_NAME

source $WORK_DIR/../mlw/get_svn_path.sh
getSvnPath "$1" "$2" "test"

if [ -z "$TRUNK_SVN_PATH" ];then
  exit
fi

echo "check out svn path $TRUNK_SVN_PATH to $SRC_DIR in 2 seconds..."
sleep 2
export MAVEN_OPTS=-Xmx512m

rm -fr $SRC_DIR
if [ ! -d "$SRC_DIR" ];then
  mkdir -p  $SRC_DIR
fi 
if [ ! -d "$BAK_DIR" ];then
  mkdir -p  $BAK_DIR
fi

echo "rm -f $RELEASE_DIR/${PROJECT_NAME}*"
rm -f $RELEASE_DIR/${PROJECT_NAME}*

svn co $TRUNK_SVN_PATH $SRC_DIR

cd $SRC_DIR

mvn -DskipTests=true clean package -U

if [ $? == 0 ];then

  if [ "mlw-web-res" == "$1" ];then
    for f in `find $SRC_DIR -name "${PROJECT_NAME}*SNAPSHOT.tar.gz" | grep -v javadoc | grep -v sources`
      do 
        echo "move target file $f to $RELEASE_DIR"
        mv $f $RELEASE_DIR
      done

    echo "$WORK_DIR/release_res.sh $1" 
    $WORK_DIR/release_res.sh $1
    exit
  fi

  for f in `find $SRC_DIR -name "${PROJECT_NAME}*SNAPSHOT.war" | grep -v javadoc | grep -v sources`
    do
      echo "move target file $f to $RELEASE_DIR"
      mv $f $RELEASE_DIR
    done

  echo "$WORK_DIR/release_web.sh $1"
  $WORK_DIR/release_web.sh $1
else
  echo "compile failure "
  #mvn release:rollback
fi



