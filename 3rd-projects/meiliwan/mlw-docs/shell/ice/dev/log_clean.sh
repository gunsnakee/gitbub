#!/bin/bash

LOG_BASE_DIR=/data/iceservice/node/logs

cd $LOG_BASE_DIR
> $LOG_BASE_DIR/stdout.log
> $LOG_BASE_DIR/stderr.log

rm -f `ls ./*/*$(date -d'6 days ago' "+%Y%m%d").log`

#CURR_DATE=`date +%Y%m%d`
#for logFile in `find ./ -type f | grep -v $CURR_DATE | grep service`
#  do
#    rm -f $logFile
#  done
