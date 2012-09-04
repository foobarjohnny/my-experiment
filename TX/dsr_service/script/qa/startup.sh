#! /bin/bash

JAVALIB=../lib

for jar in $JAVALIB/*.jar

do echo $CLASSPATH | grep $jar > /dev/null;

     if [ $? -eq 1 ]; then        

          CLASSPATH=$CLASSPATH:$jar;

     fi

done

echo $CLASSPATH
JAVA_BIN=java
if test -f $JAVA_HOME/bin/java
then
    JAVA_BIN=$JAVA_HOME/bin/java 
fi
echo Java command is $JAVA_BIN
nohup $JAVA_BIN -Xdebug -Xrunjdwp:transport=dt_socket,address=7888,server=y,suspend=n -Djava.library.path=../native/ -server -DTADS_ID=DSR-CServer -Xms256m -Xmx1000m -cp ../config:$CLASSPATH com.telenav.cserver.dsr.streaming.server.ServerStartup > nohup.out & 
