sed -i 's/INSERT INTO/INSERT ignore INTO/g' resource_initdata.sql 
sed -i 's/INSERT INTO/INSERT ignore INTO/g' 03_smartCD_component_initdata.sql 

mysql -u root -pmysql -Dsmartcd -s -N -f < /root/export_data/resource_initdata.sql
mysql -u root -pmysql -Dframe -s -N -f < /root/export_data/03_smartCD_component_initdata.sql


