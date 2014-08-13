#!/bin/bash

MLW_WEB_PROJECT='mlw-admin-web mlw-cms-web mlw-gateway-web mlw-imagecode-web mlw-monitor-web mlw-passport-web mlw-wallet-web mlw-www-web mlw-web-res'

for i in $MLW_WEB_PROJECT
	do
	./compile_web.sh $i
done


