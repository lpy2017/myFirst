#TITLE=adapter

JAVA_OPTIONS="-server -XX:PermSize=64m -XX:MaxPermSize=128m -Xmx512m -Xms256m"
export JAVA_OPTIONS

DEBUG_PORT=5200
export DEBUG_PORT

JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=${DEBUG_PORT},server=y,suspend=n"
export JAVA_DEBUG

#JAVA_PARAM="-DConfigHost=10.1.108.89 -DConfigUser=paas -DConfigPassword=aaa111 -DConfigPort=9000"
#export JAVA_PARAM

nohup ${JAVA_HOME}/bin/java ${JAVA_OPTIONS} ${JAVA_DEBUG} ${JAVA_PARAM} -jar Adapter-0.0.1-SNAPSHOT.jar >adapter.log 2>&1 &
