echo "attach the pid of jvm ... ";
jps |  awk '$2!="Jps"&&$2!="Launcher" {print $1}' > jpids;

echo "traversal every pid for jstack...";
for line in `cat jpids` ; 
    if [ -e "/data/tmp/jvm/$line/" ];
    then
      # mkdir -p /data/tmp/jvm/$line/;
    else  mkdir -p /data/tmp/jvm/$line/;
    fi
    do jstack -F $line >/data/tmp/jvm/$line/$line.jstack; 
done;

echo "traversal every pid for jstat...";
for line in `cat jpids` ; 
    do jstat -gcutil $line 250 10 > $line.jstat; 
done;

