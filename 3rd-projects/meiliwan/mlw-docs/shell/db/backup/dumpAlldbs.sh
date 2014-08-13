#!/bin/bash

#if [ -z "$1" ];then
 # echo "Please give a port like '3306' or '3306 3307 3308' "
 # exit 1
#fi

DATESTR=`date +%Y%m%d_%H%M%S`
for port in "6610" "6611" "6612" "6613" "6614"
do 
     echo "/data/mysql/bin/mysqldump -S /data/dbs/${port}file/mysql.sock --all-databases > /data/backup/initdata/$port/$DATESTR.sql 2> /data/backup/initdata/$port/logs/$DATESTR.log"
     /data/mysql/bin/mysqldump -S /data/dbs/${port}file/mysql.sock --all-databases > /data/backup/initdata/$port/$DATESTR.sql 2> /data/backup/initdata/$port/logs/$DATESTR.log
done

cd /data/backup/initdata
/bin/tar -zcvf dbs195_$DATESTR.tar.gz ./*/$DATESTR.sql
/usr/bin/rsync -vzrtopg --progress dbs195_$DATESTR.tar.gz root@10.14.16.150::dbs

#del the backup sql of 1 month ago 
echo "del the backup sql of 1 month ago"
rm -f `ls ./*/$(date -d"10 days ago" "+%Y%m%d")*.sql`
rm -f `ls ./dbs195_$(date -d"10 days ago" "+%Y%m%d")*.tar.gz`
