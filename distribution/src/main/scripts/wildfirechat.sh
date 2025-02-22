#!/bin/sh

cd "$(dirname "$0")"

# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRGDIR=`dirname "$PRG"`
    cd $PRGDIR
    PRG="`pwd`"/"$link"
  fi
done

PRGDIR=`dirname "$PRG"`
cd ..
WILDFIRECHAT_HOME=`pwd`


export WILDFIRECHAT_HOME

echo $WILDFIRECHAT_HOME

if [ -f "${JAVA_HOME}/bin/java" ]; then
   JAVA=${JAVA_HOME}/bin/java
else
   JAVA=java
fi
export JAVA

$JAVA -version

WILDFIRECHAT_CONFIG_PATH=$WILDFIRECHAT_HOME

LOG_FILE=$WILDFIRECHAT_CONFIG_PATH/config/log4j2.xml
HZ_CONF_FILE=$WILDFIRECHAT_CONFIG_PATH/config/hazelcast.xml
C3P0_CONF_FILE=$WILDFIRECHAT_CONFIG_PATH/config/c3p0-config.xml


#LOG_CONSOLE_LEVEL=info
#LOG_FILE_LEVEL=fine
JAVA_OPTS_SCRIPT="-XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true"

## Use the Hotspot garbage-first collector.
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC"

## Have the JVM do less remembered set work during STW, instead
## preferring concurrent GC. Reduces p99.9 latency.
JAVA_OPTS="$JAVA_OPTS -XX:G1RSetUpdatingPauseTimePercent=5"

## Main G1GC tunable: lowering the pause target will lower throughput and vise versa.
## 200ms is the JVM default and lowest viable setting
## 1000ms increases throughput. Keep it smaller than the timeouts.
JAVA_OPTS="$JAVA_OPTS -XX:MaxGCPauseMillis=500"

## Optional G1 Settings

# Save CPU time on large (>= 16GB) heaps by delaying region scanning
# until the heap is 70% full. The default in Hotspot 8u40 is 40%.
#JAVA_OPTS="$JAVA_OPTS -XX:InitiatingHeapOccupancyPercent=70"

# For systems with > 8 cores, the default ParallelGCThreads is 5/8 the number of logical cores.
# Otherwise equal to the number of cores when 8 or less.
# Machines with > 10 cores should try setting these to <= full cores.
#JAVA_OPTS="$JAVA_OPTS -XX:ParallelGCThreads=16"

# By default, ConcGCThreads is 1/4 of ParallelGCThreads.
# Setting both to the same value can reduce STW durations.
#JAVA_OPTS="$JAVA_OPTS -XX:ConcGCThreads=16"

### GC logging options -- uncomment to enable

#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDateStamps"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintHeapAtGC"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintTenuringDistribution"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintPromotionFailure"
#JAVA_OPTS="$JAVA_OPTS -XX:PrintFLSStatistics=1"
#JAVA_OPTS="$JAVA_OPTS -XX:+UseGCLogFileRotation"
#JAVA_OPTS="$JAVA_OPTS -XX:NumberOfGCLogFiles=10"
#JAVA_OPTS="$JAVA_OPTS -XX:GCLogFileSize=10M"

#如果JDK版本是9及以上，请打开这个配置
#JAVA_OPTS="$JAVA_OPTS --add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"

echo "警告：没有设置JVM内存参数！"
echo "请设置JVM参数Xmx和Xms，设置为您为IM服务预留的内存大小，注意需要刨除操作系统占用，如果有其它系统也需要相应去除占用，还需要减去堆外内存占用。"
echo "建议设置为总内存的60%以下，比如16G总内存，Xmx可以设置为9G"
echo ""
#JAVA_OPTS="$JAVA_OPTS -Xmx2G"
#JAVA_OPTS="$JAVA_OPTS -Xms2G"


$JAVA -server $JAVA_OPTS $JAVA_OPTS_SCRIPT -Dlog4j.configurationFile="file:$LOG_FILE" -Dlog4j2.formatMsgNoLookups=true -Dcom.mchange.v2.c3p0.cfg.xml="$C3P0_CONF_FILE" -Dhazelcast.configuration=$HZ_CONF_FILE -Dwildfirechat.path="$WILDFIRECHAT_CONFIG_PATH" -cp "$WILDFIRECHAT_HOME/lib/*" cn.wildfirechat.server.Server
