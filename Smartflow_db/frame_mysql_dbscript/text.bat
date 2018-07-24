@echo off

REM %~dp0 is name of current script under NT
set CDIR=%~dp0
set CDIR=%CDIR:~0,-1%
for %%i in ("%CDIR%") do set CDIR=%%~fsi

rem ���ݿ������
set dbhost=10.1.108.127

rem �����û�
set dbadminuser=root
rem �����û�����غͿ�������ʵ������޸�
set dbadminpwd=mysql
rem ��������û��޷���¼�����������ݿ���ִ��: grant all privileges on *.* to root@'%' identified by 'mysql' with grant option;

rem ADP���ݿ��û�
set dbuser=adp
set dbpwd=adp

rem ADP���ݿ�����
set dbname=smartframe

echo. > %CDIR%\install.log

echo ע�⣬���潫��ɾ�����ݿ� %dbname% ��Ȼ���ٰ�װ��������������ñ��ű�ɾ�����ݿ�%dbname%�������[CTL + C]����ֹ���ű��� | %CDIR%\tee -a %CDIR%\install.log
pause

echo ɾ�����ݿ� [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo DROP DATABASE IF EXISTS `%dbname%`; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

echo �������ݿ� [%dbname%] | %CDIR%\tee -a %CDIR%\install.log
echo CREATE DATABASE `%dbname%` CHARACTER SET 'gbk' COLLATE 'gbk_chinese_ci'; | mysql --default-character-set=gbk -h %dbhost% -u %dbadminuser% -p%dbadminpwd% | %CDIR%\tee -a %CDIR%\install.log

echo �����û� [%dbuser%/%dbpwd%], �����û���Ȩ | %CDIR%\tee -a %CDIR%\install.log
echo grant all privileges on *.* to %dbuser%@'%%' identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h%dbhost% -u%dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log
echo grant all privileges on *.* to %dbuser%@%dbhost% identified by '%dbpwd%'; flush privileges; | mysql --default-character-set=gbk -h%dbhost% -u%dbadminuser% -p%dbadminpwd%  | %CDIR%\tee -a %CDIR%\install.log

set dbpath=%CDIR%\mysql
echo ����ADP������... | %CDIR%\tee -a %CDIR%\install.log

mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\new_procedure.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log
mysql --default-character-set=gbk --force -h %dbhost% -u %dbuser% -p%dbpwd% %dbname%  < %dbpath%\new_procedure_model.sql 2>&1 | %CDIR%\tee -a %CDIR%\install.log

echo ���� %CDIR%\install.log ���Ƿ�����쳣��Ϣ������Ӧ��ע���²��֣� | %CDIR%\tee -a %CDIR%\install.log
find /N /I "ERROR " %CDIR%\install.log
pause

echo ���ݿ��ʼ����ϡ� | %CDIR%\tee -a %CDIR%\install.log