@title Agent
@set ConfigApp=node
@set BASE_HOME=%~dp0
echo "BASE_HOME is "%BASE_HOME%
@set CONF_DIR=%BASE_HOME%..\config
echo "-----------------------------"
echo "CONF_DIR is "%CONF_DIR%

@set DEBUG_PORT=6008
if NOT "%DEBUG_PORT%" == "" (
  @set JAVA_DEBUG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=%DEBUG_PORT%,server=y,suspend=n 
)

echo "*********************************************************************************************"
%JAVA_HOME%/bin/java -Dserver.port=5101 -Dbase.home=%BASE_HOME% -Dbase.conf=%CONF_DIR% -Dspring.config.location=%CONF_DIR%/application.properties -Dmessage.valid=true -DuseAIO=false -Dfile.encoding=utf-8 -Dcom.dc.install_path=%CONF_DIR% %JAVA_DEBUG% -jar %BASE_HOME%/../lib/NodeAgent.jar

