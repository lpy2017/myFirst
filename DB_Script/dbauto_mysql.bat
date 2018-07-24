@title CD DB init
echo ensure that the db with name in all_mysql.sql is created by this script, or there are errors...
echo "paas installer 1.0" > paas_mysql.log
echo "paas installer 1.0"

echo "Installing all_mysql.sql/ ..." >> paas_mysql.log
echo "Installing all_mysql.sql/ ..."

"C:\Program Files\MySQL\MySQL Server 5.6\bin\mysql.exe"  -u root -pmysql -h10.126.3.217 < all_mysql.sql

echo "Installing successfully..." >> paas_mysql.log
echo "Installing successfully ..."
