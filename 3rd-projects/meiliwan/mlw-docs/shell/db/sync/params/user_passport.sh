#!/bin/bash

SRC_SOCKET_FILE=/data/dbs/3306file/mysql.sock
#db name
SRC_DB=mlw_user
#TIME_FIELD used to determine to export data
TIME_FIELD=login_time
#export fields, can use '*' to export all fields
SRC_FILEDS="uid,user_name,nick_name,email,mobile,create_time,email_active,mobile_active,login_time,login_equip,login_ip,login_equip_id,state,user_level"

TARGET_IP=10.249.15.141
TARGET_PORT=3306
TARGET_USER=13mlw
TARGET_PASSWD=mlwhappy543
TARGET_DB=mlw_dc_base

