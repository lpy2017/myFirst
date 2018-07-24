export ConfigApp=node

BASE_HOME=$(cd $(dirname $0) && pwd)
export BASE_HOME
echo "BASE_HOME is "$BASE_HOME

CONF_DIR=$BASE_HOME/../config
export CONF_DIR
echo "-----------------------------"
echo "CONF_DIR is "$CONF_DIR

JAVA_REMOTE_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=6008,server=y,suspend=n"
export JAVA_REMOTE_DEBUG

#iptables -F
#iptables -t nat -F

echo "*********************************************************************************************"
$JAVA_HOME/bin/java -Dfile.encoding=UTF-8 -Dserver.port=5101 -Dbase.home=$BASE_HOME/ -Dbase.conf=${CONF_DIR} -Dcom.dc.install_path=${CONF_DIR} -Dspring.config.location=${CONF_DIR}/application.properties -Dmessage.valid=true -DuseAIO=false $JAVA_REMOTE_DEBUG -jar $BASE_HOME/../lib/NodeAgent.jar