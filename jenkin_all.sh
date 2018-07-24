#tomcatPath=/home/apache-tomcat-8.5.9/webapps/repo
buildtime=$(date +%Y%m%d%H%M%S)

#adapter build
mkdir -p build/SmartCD/Adapter

cp Adapter/src/main/resources/script/start.sh build/SmartCD/Adapter/start.sh
cp Adapter/target/Adapter-0.0.1-SNAPSHOT.jar build/SmartCD/Adapter/Adapter-0.0.1-SNAPSHOT.jar



#master build
mkdir -p build/SmartCD/AppMaster

cp AppMaster/src/main/resources/script/start.sh build/SmartCD/AppMaster/start.sh
cp AppMaster/target/AppMaster.jar build/SmartCD/AppMaster/AppMaster.jar


#CloudUI build
mkdir -p build/SmartCD/CloudUI

cp CloudUI/src/main/resources/script/start.sh build/SmartCD/CloudUI/start.sh
cp CloudUI/target/cloudui.jar build/SmartCD/CloudUI/cloudui.jar


#DBscript
cp configcenter.sql DB_Script
tar -cvf DB_Script.tar  DB_Script

#smartCD_db
if [ -d smartCD_db ]
then
	sudo rm -rf smartCD_db
fi
cp -r DB_Script smartCD_db
sed -i 's/^source configcenter.sql/#source configcenter.sql/g' smartCD_db/all_mysql.sql
sed -i 's/^USE `configcenter`/#USE `configcenter`/g' smartCD_db/all_mysql.sql
sed -i 's/^CREATE.*configcenter.*/#CREATE DATABASE IF NOT EXISTS `configcenter` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci/g' smartCD_db/all_mysql.sql
tar -cvf smartCD_db.tar smartCD_db

cd build/SmartCD/
tar -cvf Adapter.tar  Adapter
tar -cvf AppMaster.tar AppMaster
tar -cvf CloudUI.tar CloudUI

cd ../..
cp -r ./DB_Script.tar  ${tomcatPath}
cp -r ./smartCD_db.tar ${tomcatPath}
cp -r ./build/SmartCD/Adapter.tar  ${tomcatPath}
cp -r ./build/SmartCD/AppMaster.tar  ${tomcatPath}
cp -r ./build/SmartCD/CloudUI.tar  ${tomcatPath}
cp -r ./NodeAgent/target/NodeAgent-bin.tar  ${tomcatPath}