#!/usr/bin/env bash

. /etc/profile
. ~/.bash_profile

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

source "$bin"/env.sh

cd $bin
java -Xmx4048m -cp .:lib/*:conf com.meiliwan.tongji.tongji1.SourceOrderStat 2014-04-13
