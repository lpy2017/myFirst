mkdir -p build/SmartCD/sql
mkdir -p build/SmartCD/Adapter
cp Adapter/src/main/resources/script/start.sh build/SmartCD/Adapter/start.sh
cp Adapter/target/Adapter-0.0.1-SNAPSHOT.jar build/SmartCD/Adapter/Adapter-0.0.1-SNAPSHOT.jar
cp Adapter/src/main/resources/sql/adapter.sql build/SmartCD/sql/adapter.sql
cd build
tar -zcvf SmartCD.tar.gz SmartCD/

