@echo off

REM %~dp0 is name of current script under NT
set CDIR=%~dp0
set CDIR=%CDIR:~0,-1%
for %%i in ("%CDIR%") do set CDIR=%%~fsi

rem ���ݿ������
set dbhost=10.126.3.220

rem �����û�
set dbadminuser=root
rem �����û�����غͿ�������ʵ������޸�
set dbadminpwd=mysql
rem ��������û��޷���¼�����������ݿ���ִ��: grant all privileges on *.* to root@'%' identified by 'mysql' with grant option;

rem ADP���ݿ��û�
set dbuser=root
set dbpwd=mysql

rem ADP���ݿ�����
set dbname=frame0206

echo > %CDIR%\install.log

echo ע�⣬���潫��ɾ�����ݿ�%dbname%��Ȼ���ٰ�װ��������������ñ��ű�ɾ�����ݿ�%dbname%�������[CTL + C]����ֹ���ű��� | %CDIR%\tee -a %CDIR%\install.log
pause

echo ɾ�����ݿ� [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo DROP DATABASE IF EXISTS `%dbname%`; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

echo �������ݿ� [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo CREATE DATABASE `%dbname%` CHARACTER SET 'gbk' COLLATE 'gbk_chinese_ci'; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

rem echo �����û� [%dbuser%/%dbpwd%], �����û���Ȩ | %CDIR%\tee -a %CDIR%\install.log
rem echo grant all privileges on *.* to %dbuser%@'%%' identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log
rem echo grant all privileges on *.* to %dbuser%@%dbhost% identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log

set dbpath=%CDIR%\mysql
echo ����ADP������... | %CDIR%\tee -a %CDIR%\install.log
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


echo ���� %CDIR%\install.log ���Ƿ�����쳣��Ϣ������Ӧ��ע���²��֣� | %CDIR%\tee -a %CDIR%\install.log
find /N /I "ERROR " %CDIR%\install.log
pause

echo ���ݿ��ʼ����ϡ� | %CDIR%\tee -a %CDIR%\install.log
