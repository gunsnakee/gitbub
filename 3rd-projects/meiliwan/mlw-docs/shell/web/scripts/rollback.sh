#!/bin/bash

PREFIX=$1
if [ -z "$1" ]; then
  echo "Please give a web prefix , available prefix are follow:"
  echo "  www,admin,account,gateway,passport,monitor,cms"
  exit 1
fi
WAR_NAME=mlw-${PREFIX}-web.war
WORK_DIR=/data/www/$PREFIX.meiliwan.com/ROOT
BAK_DIR=/data/bak/$PREFIX.meiliwan.com
NEW_FILE=$BAK_DIR/$WAR_NAME
CONF_DIR=/data/www/$PREFIX.meiliwan.com/online_conf

echo "ls -rt $BAK_DIR | awk -F'war_' '{print \$2}'"
ls -rt $BAK_DIR | awk -F'war_' '{print $2}'

echo "Please select a version from the list above"
read -t 300 -p "Please select a version from the list above: " DATESTR
if [ -z "$DATESTR" ];then
  echo "version cannot be null,program will exit."
  exit
fi

NEW_FILE=${NEW_FILE}_${DATESTR}

if [ -f $NEW_FILE ];then
  echo "exec /data/software/tomcat-$PREFIX/bin/shutdown.sh"
  /data/software/tomcat-$PREFIX/bin/shutdown.sh

  echo "sleep 3 seconds ..."
  sleep 3

  #杀掉当前正在运行的tomcat进程
  CURRPID=`ps aux | grep "tomcat-$PREFIX" | grep java | awk '{print $2}'`
  if [ ! -z "$CURRPID" ];then
    echo "kill -9 $CURRPID"
    kill -9 $CURRPID
  fi

  echo "found $NEW_FILE , then begin redeploy it in $WORK_DIR"
  cd $WORK_DIR
  
  echo "rm -fr $WORK_DIR/*"
  rm -fr $WORK_DIR/*

  if [ -d /data/software/tomcat-$PREFIX/work ];then
    echo "rm -fr /data/software/tomcat-$PREFIX/work/*"
    rm -fr /data/software/tomcat-$PREFIX/work/*
  fi

  echo "cp $NEW_FILE $WORK_DIR"
  cp $NEW_FILE $WORK_DIR/$WAR_NAME

  echo "/usr/java/default/bin/jar -xf $WAR_NAME && rm -f $WORK_DIR/$WAR_NAME"
  /usr/java/default/bin/jar -xf $WAR_NAME && rm -f $WORK_DIR/$WAR_NAME

  echo "cp -r $CONF_DIR/* $WORK_DIR/WEB-INF/classes"
  cp -r $CONF_DIR/* $WORK_DIR/WEB-INF/classes
  
  echo "exec /data/software/tomcat-$PREFIX/bin/startup.sh"
  /data/software/tomcat-$PREFIX/bin/startup.sh  

  echo "sleep 15 seconds to check the catalina.out"
  sleep 15

  echo "tail -n300 /data/software/tomcat-$PREFIX/logs/catalina.out"
  tail -n300 /data/software/tomcat-$PREFIX/logs/catalina.out
  
else
  echo "$NEW_FILE has not found, then exit."
  exit
fi


