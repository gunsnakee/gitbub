#!/usr/bin/env bash

. /etc/profile
. ~/.bash_profile

bin=`dirname "$0"`
bin=`cd "$bin/.."; pwd`

PROJECT_HOME=$bin

JAVA_HOME=${JAVA_HOME:-/usr/lib/jvm/java}


export JAVA_HOME

CLASSPATH=${CLASSPATH}:$JAVA_HOME/lib/tools.jar:${PROJECT_HOME}/conf

for f in ${bin}/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
CLASSPATH=${CLASSPATH}:${bin}/conf;
export CLASSPATH

PROJECT_DAEMON_PIDFILE=$bin/pids/cr.pid
DAEMON_LOG_FILE=$bin/logs/cr.log

JAVA_EXEC=$JAVA_HOME/bin/java
JAVA_OPTS="-Xmx2048m -Xms248m"


JOB1="com.meiliwan.recommend.job.RecommendBuilder 0"

$JAVA_EXEC $JAVA_OPTS -classpath "$CLASSPATH" $JOB1



