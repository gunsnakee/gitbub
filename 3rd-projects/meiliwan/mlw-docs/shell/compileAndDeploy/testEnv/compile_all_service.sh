#!/bin/bash

GROUP_NAME=$1
MLW_COMMONS_PROJECT='mlw-commons-db mlw-commons-ice mlw-commons-util'
MLW_CORE_PROJECT='mlw-gateway mlw-oms mlw-passport mlw-pms mlw-sp mlw-stock mlw-wallet'
MLW_SUPPORT_PROJECT='mlw-admin mlw-antispam mlw-base mlw-cms mlw-monitor mlw-mq-job'

if [ -z $GROUP_NAME ];then
  echo "Please define an group module name."
  echo "mlw-commons mlw-core mlw-support"
  echo " "
  exit 1
fi

if [ "$1" == "mlw-commons" ];then
	for i in $MLW_COMMONS_PROJECT
		do
		./compile.sh $i
	done
	
elif [ "$1" == "mlw-core" ];then
	for i in $MLW_CORE_PROJECT
		do
		./compile.sh $i
	done
	
elif [ "$1" == "mlw-support" ];then
	for i in $MLW_SUPPORT_PROJECT
		do
		./compile.sh $i
	done
	
else
	echo ""
  echo "group name is not supported by the compile program"
fi

