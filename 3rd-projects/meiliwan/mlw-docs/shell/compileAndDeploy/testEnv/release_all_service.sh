#!/bin/bash

GROUP_NAME=$1
MLW_PROJECT='mlw-oms mlw-pms mlw-mms mlw-cms mlw-account mlw-bkstage mlw-base mlw-pay mlw-monitor mlw-antispam mlw-stock'

echo "release all project "
echo "oms pms mms cms account bkstage base pay monitor antispam stock "
echo "beginning ... "

for i in $MLW_PROJECT
  do
    /data/compileAnddeploy/testEnv/release.sh $i
done

echo " "
echo "THE END "
