#!/bin/sh
echo ensure that the db with name in all_mysql.sql is created by this script, or there are errors...
echo "paas installer 1.0" > paas_mysql.log
echo "paas installer 1.0"

echo "Installing all_mysql.sql/ ..." >> paas_mysql.log
echo "Installing all_mysql.sql/ ..."

mysql -u root -pmysql -h127.0.0.1 < all_mysql.sql

echo "Installing successfully..." >> paas_mysql.log
echo "Installing successfully ..."
