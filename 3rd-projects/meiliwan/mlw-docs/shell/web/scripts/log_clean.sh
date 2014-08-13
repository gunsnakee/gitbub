#!/bin/bash

source /etc/profile

DAYS=6
if [ ! -z $1 ];then
  DAYS=$1
fi

TOMCAT_BASE_DIR=/data/software
WWW_LOG_DIR=/data/www/logs
DAEMON_BASE_DIR=/data/software/mq_app

rm -f `ls $TOMCAT_BASE_DIR/tomcat*/logs/*$(date -d"$DAYS days ago" "+%Y-%m-%d")*`
rm -f `ls $WWW_LOG_DIR/*/*$(date -d"$DAYS days ago" "+%Y%m%d").log`
rm -f `ls $DAEMON_BASE_DIR/*/logs/*$(date -d"$DAYS days ago" "+%Y%m%d").log`
