#!/bin/bash
#
echo "PID of this script: $$"
JAVA_OPTIONS="-server -XX:PermSize=64m -XX:MaxPermSize=128m -Xmx512m -Xms256m"
export JAVA_OPTIONS
echo ${JAVA_HOME}
#DEBUG_PORT=5006
DEBUG_PORT=18000
export DEBUG_PORT

BASE_HOME=$(cd $(dirname $0) && pwd)
export BASE_HOME
echo "BASE_HOME is "$BASE_HOME

JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n"
export JAVA_DEBUG

#JAVA_PARAM="-DConfig_host=10.1.108.89 -DConfig_user=paas -DConfig_password=aaa111 -DConfig_app=cloudui"
#export JAVA_PARAM

#nohup ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} ${JAVA_DEBUG} ${JAVA_PARAM} -jar CloudUI-0.0.1-SNAPSHOT.jar >cloudui.log 2>&1 &

nohup ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} ${JAVA_DEBUG} ${JAVA_PARAM} -Dfile.encoding=UTF-8 -Dspring.config.location=${BASE_HOME}/application.properties -jar /home/cloudui/cloudui.jar >/home/cloudui/cloudui.log 2>&1 &