#!/bin/bash
MNAME=$1
PORTLIST=$2
cip=$3
shortname=${MNAME#*'CN'}

IFS=',' 
list=${PORTLIST}
for PORT in ${list[@]}
do
nc -v -w 5 -z $cip $PORT

flag=`echo $?`

if [ "0" != "$flag" ]
	then
	nodeConf=$(dirname $(cd $(dirname $0) && pwd))
        sudo mkdir -p $nodeConf/log
        filename=$nodeConf/log/$MNAME"_"$(date +%Y%m%d_%H%M%S).txt
        sudo netstat -anp|grep -w $PORT >>$filename
	echo "------">>$filename
        sudo route -n>>$filename
        echo "------">>$filename
        docker exec $MNAME ss -anpt|grep -w ':'$PORT>>$filename
        echo "------">>$filename
        ifconfig>>$filename
        echo "------">>$filename
        docker exec $MNAME ifconfig>>$filename
        echo "------">>$filename
        docker exec $MNAME nc -v -w 5 -z $cip $PORT 2>>$filename
        echo "------">>$filename
        docker exec $MNAME route -n>>$filename
        echo "------">>$filename
        sudo arp -a>>$filename


fi

done
