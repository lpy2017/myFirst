#TITLE=master

BASE_HOME=$(cd $(dirname $0) && pwd)
export BASE_HOME
echo "BASE_HOME is "$BASE_HOME

JAVA_OPTIONS="-server -XX:PermSize=64m -XX:MaxPermSize=1024m -Xmx1024m -Xms256m"
export JAVA_OPTIONS

DEBUG_PORT=19000
export DEBUG_PORT

JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n"
export JAVA_DEBUG

#ZK="-Dzoo.cfg.path=/datafs/zk/conf/zoo.cfg"
#export ZK

nohup ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} ${JAVA_DEBUG} -Dfile.encoding=UTF-8 -Dspring.config.location=${BASE_HOME}/application.properties -Dorg.quartz.properties=${BASE_HOME}/quartz.properties -jar AppMaster.jar > master.log 2>&1 &