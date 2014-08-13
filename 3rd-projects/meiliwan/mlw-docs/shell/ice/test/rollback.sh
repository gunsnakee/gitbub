#!/bin/bash

if [ -z $1 ]; then
  echo "Please give a module name..."
  exit 1
fi

LIB_DIR=/data/iceservice/node/lib/$1
BAK_DIR=/data/backup/$1

if [ -d $LIB_DIR ]; then

  if [ ! -d $BAK_DIR ]; then
    echo "mkdir -p $BAK_DIR"
    mkdir -p $BAK_DIR
  fi

  ls -rt $BAK_DIR | awk -F'gz_' '{print $2}' 

  echo "Please select a version from the list above"
  read -t 300 -p "Please select a version from the list above: " DATESTR
  if [ -z "$DATESTR" ];then
    echo "version could not be null, Please run this program again."
    echo ""
    exit
  fi

  if [ ! -f "$BAK_DIR/$1.tar.gz_${DATESTR}" ];then
    echo "file $BAK_DIR/$1.tar.gz_${DATESTR} not exists, then exit."
    exit
  fi

  cd $LIB_DIR 
  echo "rm -fr *"
  rm -fr *

  echo "cp $BAK_DIR/$1.tar.gz_${DATESTR} $LIB_DIR"
  cp $BAK_DIR/$1.tar.gz_${DATESTR} $LIB_DIR/$1.tar.gz
    
  echo "tar -zxf $1.tar.gz"
  tar -zxf $1.tar.gz

  echo "rm -f $1.tar.gz"
  rm -f $1.tar.gz

  echo "/data/iceservice/restartIceBox.sh $1 $2"
  /data/iceservice/restartIceBox.sh $1 $2

else
  echo "directory $LIB_DIR not exists."
fi
