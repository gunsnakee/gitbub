#!/bin/bash

MYSQL_BIN=/data/mysql/bin/mysql

for TB_PARAM_FILE in `find /data/scripts/params -type f | grep ^.*sh$`
  do
    FILE_NAME=`basename $TB_PARAM_FILE`   
    TB_NAME=${FILE_NAME%.*}
    PARAM_FILE=/data/scripts/params/${TB_NAME}.sh
    TIME_FILE=/data/scripts/params/${TB_NAME}.tm

    if [ ! -f "$PARAM_FILE" ];then
      echo "$PARAM_FILE not exists, then exit."
      exit 1
    fi

    source $PARAM_FILE

    WHERE_COND=""
    if [ -f "$TIME_FILE" -a ! -z "$TIME_FIELD" ];then
      START_TIME=`cat $TIME_FILE`
      if [ ! -z "$START_TIME" ];then
        WHERE_COND=" where $TIME_FIELD >= '$START_TIME' "
      fi
    fi

    echo `date '+%Y-%m-%d %H:%M:%S'` > $TIME_FILE

    rm -f "/tmp/${TB_NAME}.txt"

    echo "begin to export into outfile /tmp/${TB_NAME}.txt"
    $MYSQL_BIN -S $SRC_SOCKET_FILE $SRC_DB -e "select $SRC_FILEDS from $TB_NAME $WHERE_COND into outfile '/tmp/${TB_NAME}.txt'"

    if [ -f "/tmp/${TB_NAME}.txt" ];then
      $MYSQL_BIN -h $TARGET_IP -P $TARGET_PORT -u$TARGET_USER -p$TARGET_PASSWD $TARGET_DB -e "load data local infile '/tmp/${TB_NAME}.txt' REPLACE into table $TB_NAME fields terminated by '\t' lines terminated by '\n' "
    else
      echo "data of table '$TB_NAME' export failure into /tmp/${TB_NAME}.txt ."
    fi

  done
