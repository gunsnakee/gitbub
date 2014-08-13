#!/usr/bin/env bash

. /etc/profile
. ~/.bash_profile

bin=`dirname "$0"`
bin=`cd "$bin"; pwd`

source "$bin"/env.sh

cd $bin
java -Xmx1048m -cp .:lib/* com.meiliwan.search.spell.data.ProductTitleSpellAnalyzer true 
