#!/bin/bash

#source /etc/profile

function restartIceServer(){
  local REGISTER_IP=10.249.15.194
  echo "$REGISTER_IP , $1"
  /data/software/ice/bin/icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server stop $1"
  /data/software/ice/bin/icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server disable $1"
  /data/software/ice/bin/icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server enable $1"
  /data/software/ice/bin/icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server start $1"
}

function restartAll(){
  local REGISTER_IP=10.2.254.55
  for i in `icegridadmin -u mop -p happy666  --Ice.Default.Locator='IceGrid/Locator:tcp -h 10.2.254.55 -p 4061' -e "server list"`
  do
    echo $i
    icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server stop $i"
    icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server disable $i"
    icegridadmin -u mop -p happy666 --Ice.Default.Locator="IceGrid/Locator:tcp -h $REGISTER_IP -p 4061" -e "server enable $i"

    sleep 1
  done
}

if [ $# -lt  1 ];then
  echo "WARN: Please define a module name."
  exit
fi

ICEBOX_NAME=$1
ICEBOX_NAME=${ICEBOX_NAME##*-}IceBox$2
echo "ICEBOX_NAME $ICEBOX_NAME"
restartIceServer $ICEBOX_NAME
