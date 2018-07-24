@echo off
title Quartz

set BASE_HOME=%~dp0
echo "BASE_HOME is "%BASE_HOME%

rem set JAVA_OPTIONS="-server -Xmx512m"

set DEBUG_PORT=5301

set JAVA_DEBUG="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=%DEBUG_PORT%,server=y,suspend=n"

cd /d %BASE_HOME%
"%JAVA_HOME%\bin\jar" uvf Quartz.jar BOOT-INF/classes/quartz.properties

"%JAVA_HOME%\bin\java" %JAVA_OPTIONS% %JAVA_DEBUG% -Dspring.config.location=%BASE_HOME%/application.properties -jar %BASE_HOME%/Quartz.jar