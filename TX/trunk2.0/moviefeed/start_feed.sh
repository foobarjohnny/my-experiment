cd /home/tnuser/movies_feed
JAVALIB=./lib

for jar in $JAVALIB/*.jar
do echo $CLASSPATH | grep $jar > /dev/null;
     if [ $? -eq 1 ]; then
          CLASSPATH=$CLASSPATH:$jar;
     fi
done

dt=`date +'%Y%m%d%H%M%S'`

logfile=nohup.out

nohup /usr/java/jdk1.6.0_12/bin/java -Xms256m -Xmx512m -Djava.awt.headless=true -cp config:.:$CLASSPATH com.telenav.browser.movie.vendor.FeedTask > $logfile &

echo '[INFO]Movie feed has been started'
