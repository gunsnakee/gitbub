#!/bin/bash

SVN_TYPE="trunk"

TRUNK_SVN_PATH=""
TAG_BASE=""
BRANCH_BASE=""

if [ ! -z "$2" ];then
  SVN_TYPE=branches/${1}-$2
  #SVN_TYPE=branches/$2
fi

function getSvnPath ()
{
  if [ "$1" == "mlw-commons-db" -o "$1" == "mlw-commons-ice" -o "$1" == "mlw-commons-util" ];then
    TRUNK_SVN_PATH=http://reform.mop.com/newsvn/projects/meiliwan/mlw-commons/$1/$SVN_TYPE
    TAG_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-commons/$1/tags
    BRANCH_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-commons/$1/branches
  elif [ "$1" == "mlw-gateway" -o "$1" == "mlw-oms" -o "$1" == "mlw-passport" -o "$1" == "mlw-pms" -o "$1" == "mlw-sp" -o "$1" == "mlw-stock" -o "$1" == "mlw-wallet" ];then
    TRUNK_SVN_PATH=http://reform.mop.com/newsvn/projects/meiliwan/mlw-core/$1/$SVN_TYPE
    TAG_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-core/$1/tags
    BRANCH_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-core/$1/branches
  elif [ "$1" == "mlw-admin" -o "$1" == "mlw-antispam" -o "$1" == "mlw-base" -o "$1" == "mlw-cms" -o "$1" == "mlw-monitor" -o "$1" == "mlw-mq-job" ]; then
    TRUNK_SVN_PATH=http://reform.mop.com/newsvn/projects/meiliwan/mlw-support/$1/$SVN_TYPE
    TAG_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-support/$1/tags
    BRANCH_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-support/$1/branches
  elif [ "$1" == "mlw-admin-web" -o "$1" == "mlw-cms-web" -o "$1" == "mlw-gateway-web" -o "$1" == "mlw-imagecode-web" -o "$1" == "mlw-monitor-web" -o "$1" == "mlw-passport-web" -o "$1" == "mlw-wallet-web" -o "$1" == "mlw-www-web" -o "$1" == "mlw-web-res"  ];then
    TRUNK_SVN_PATH=http://reform.mop.com/newsvn/projects/meiliwan/mlw-web/$1/$SVN_TYPE
    TAG_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-web/$1/tags
    BRANCH_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-web/$1/branches
  elif [ "$1" == "mlw-tongji-task" ];then
    TRUNK_SVN_PATH=http://reform.mop.com/newsvn/projects/meiliwan/mlw-dc/$1/$SVN_TYPE 
    TAG_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-dc/$1/tags
    BRANCH_BASE=http://reform.mop.com/newsvn/projects/meiliwan/mlw-dc/$1/branches
  else
    echo ""
    echo "project name is not supported by the compile program"
  fi

  if [ "trunk" == "${TRUNK_SVN_PATH##*/}" -a "test" == "$3" ];then
    VER="5.0.1"
    read -t 300 -p "Please give a branch version(default $VER): " VER

    if [ -z "$VER" ];then
      VER="5.0.1"
    fi
    DATESTR=`date +%Y%m%d_%H%M%S`
    echo "svn cp $TRUNK_SVN_PATH $BRANCH_BASE/${1}-${VER} -m \"branch from trunk at $DATESTR \""
    svn cp $TRUNK_SVN_PATH $BRANCH_BASE/${1}-${VER} -m "branch from trunk at $DATESTR" && TRUNK_SVN_PATH=$BRANCH_BASE/${1}-${VER}
    
  fi
}
