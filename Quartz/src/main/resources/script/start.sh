#TITLE=quartz

BASE_HOME=$(cd $(dirname $0) && pwd)
export BASE_HOME

JAVA_OPTIONS="-server -Xmx512m"
export JAVA_OPTIONS

DEBUG_PORT=5301
export DEBUG_PORT

JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n"
export JAVA_DEBUG

cd ${BASE_HOME}

${JAVA_HOME}/bin/jar uvf Quartz.jar BOOT-INF/classes/quartz.properties

nohup ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} ${JAVA_DEBUG} -Dspring.config.location=${BASE_HOME}/application.properties -Dorg.quartz.properties=${BASE_HOME}/quartz.properties -jar Quartz.jar > quartz.log 2>&1 &
