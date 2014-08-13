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

  cd $LIB_DIR 
  echo "tar -zcf $1.tar.gz *"
  tar -zcf $1.tar.gz *

  DATESTR=`date +%Y%m%d_%H%M`
  mv $1.tar.gz $BAK_DIR/$1.tar.gz_${DATESTR}

  echo "rm -f mlw*"
  rm -f mlw*
else
  echo "directory $LIB_DIR not exists."
fi
