#!/bin/bash

MLW_WEB_PROJECT='mlw-admin-web mlw-cms-web mlw-gateway-web mlw-imagecode-web mlw-monitor-web mlw-passport-web mlw-wallet-web mlw-www-web'

echo "release all web project "
echo "mlw-admin-web mlw-cms-web mlw-gateway-web mlw-imagecode-web mlw-monitor-web mlw-passport-web mlw-wallet-web mlw-www-web"
echo "beginning ... "

for i in $MLW_WEB_PROJECT
	do
	/data/compileAnddeploy/testEnv/release_web.sh $i
done

echo " "
echo "THE END"

