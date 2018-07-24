@echo off

REM %~dp0 is name of current script under NT
set CDIR=%~dp0
set CDIR=%CDIR:~0,-1%
for %%i in ("%CDIR%") do set CDIR=%%~fsi

rem 数据库服务器
set dbhost=10.1.108.127

rem 超级用户
set dbadminuser=root
rem 超级用户口令开关和口令。请根据实际情况修改
set dbadminpwd=mysql
rem 如果超级用户无法登录，请先在数据库中执行: grant all privileges on *.* to root@'%' identified by 'mysql' with grant option;

rem ADP数据库用户
set dbuser=adp
set dbpwd=adp

rem ADP数据库名称
set dbname=smartframe

echo. > %CDIR%\install.log

echo 注意，下面将现删除数据库 %dbname% ，然后再安装！如果您不打算让本脚本删除数据库%dbname%，请键入[CTL + C]，终止本脚本。 | %CDIR%\tee -a %CDIR%\install.log
pause

echo 删除数据库 [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo DROP DATABASE IF EXISTS `%dbname%`; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

echo 创建数据库 [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo CREATE DATABASE `%dbname%` CHARACTER SET 'gbk' COLLATE 'gbk_chinese_ci'; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

echo 创建用户 [%dbuser%/%dbpwd%], 并给用户授权 | %CDIR%\tee -a %CDIR%\install.log
echo grant all privileges on *.* to %dbuser%@'%%' identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h%dbhost% -u%dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log
echo grant all privileges on *.* to %dbuser%@%dbhost% identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h%dbhost% -u%dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log

set dbpath=%CDIR%\mysql
echo 加载ADP的数据... | %CDIR%\tee -a %CDIR%\install.log

mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\new_procedure.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\new_procedure_model.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log

echo 请检查 %CDIR%\install.log 中是否存在异常信息，尤其应关注以下部分： | %CDIR%\tee -a %CDIR%\install.log
find /N /I "ERROR " %CDIR%\install.log
pause

echo 数据库初始化完毕。 | %CDIR%\tee -a %CDIR%\install.log