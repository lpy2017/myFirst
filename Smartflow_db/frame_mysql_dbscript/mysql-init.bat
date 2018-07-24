@echo off

REM %~dp0 is name of current script under NT
set CDIR=%~dp0
set CDIR=%CDIR:~0,-1%
for %%i in ("%CDIR%") do set CDIR=%%~fsi

rem 数据库服务器
set dbhost=10.126.3.220

rem 超级用户
set dbadminuser=root
rem 超级用户口令开关和口令。请根据实际情况修改
set dbadminpwd=mysql
rem 如果超级用户无法登录，请先在数据库中执行: grant all privileges on *.* to root@'%' identified by 'mysql' with grant option;

rem ADP数据库用户
set dbuser=root
set dbpwd=mysql

rem ADP数据库名称
set dbname=frame0206

echo > %CDIR%\install.log

echo 注意，下面将现删除数据库%dbname%，然后再安装！如果您不打算让本脚本删除数据库%dbname%，请键入[CTL + C]，终止本脚本。 | %CDIR%\tee -a %CDIR%\install.log
pause

echo 删除数据库 [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo DROP DATABASE IF EXISTS `%dbname%`; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

echo 创建数据库 [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo CREATE DATABASE `%dbname%` CHARACTER SET 'gbk' COLLATE 'gbk_chinese_ci'; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

rem echo 创建用户 [%dbuser%/%dbpwd%], 并给用户授权 | %CDIR%\tee -a %CDIR%\install.log
rem echo grant all privileges on *.* to %dbuser%@'%%' identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log
rem echo grant all privileges on *.* to %dbuser%@%dbhost% identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log

set dbpath=%CDIR%\mysql
echo 加载ADP的数据... | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\DM_DWLSGX.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_system.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\dm_ywhj.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gnmk.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gnmk_tree.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\F_FRAME_SEQUENCE.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_SQL_STATISTICS.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
rem mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\SEQ_FR_SQL_STATISTICS.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_INNER_SERVICEINFO.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_PROPERTIES_SERVICEINFO.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_SERVICERELATION.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_USER_CZ_EXCEPTION_LOG-FR_USER_CZ_LOG.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_ACTION_STATISTICS.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_SERVICE_STATISTICS.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
rem mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\SEQ_FR_ACTION_STATISTICS.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
rem mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\SEQ_FR_SERVICE_STATISTICS.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_BUSINESS_TJ_LOG.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_ONLINE_USER.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\DM_JG____DM_CZRY____QX_USER.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\FR_SWITCH.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gnmb.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gnmb_gnmk.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gw.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gw_gnmb.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_user_gw.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\dm_mklx.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\DM_XZQH.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gnmb_gnmk_operation.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_operation.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\message_type.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE_FIELD_DEFINITION.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE_EXTENSION.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE_FIELD_DISPLAY.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE_SYSTEM---MESSAGE_SYSTEM_FIELD---MESSAGE_FIELD_MAPPING---MESSAGE_FIELD_RENDER.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE_COMMON.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\MESSAGE_COMMON_OTM.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\xt_xtcs.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_gzz.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\qx_czry_gzz.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\QX_FAV_GNMK_TREE.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\SEC_PARAM.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\fr_file.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\quartz.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\new_procedure.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\new_procedure_model.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=utf8 --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\..\..\MySql\full\01_sfwf_createtable.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=utf8 --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\..\..\MySql\full\02_sfwf_initdata.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log

mysql --default-character-set=utf8 --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\..\..\MySql\full\03_smartCD_component_initdata.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log


echo 请检查 %CDIR%\install.log 中是否存在异常信息，尤其应关注以下部分： | %CDIR%\tee -a %CDIR%\install.log
find /N /I "ERROR " %CDIR%\install.log
pause

echo 数据库初始化完毕。 | %CDIR%\tee -a %CDIR%\install.log
