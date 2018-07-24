#!/bin/bash
MNAME=$1
PORT=$2
NETADDR=$3
shortname=${MNAME#*'CN'}

sudo nc -v -w 5 -z ${NETADDR}.${shortname} $PORT

flag=`echo $?`

if [ "0" != "$flag" ]
	then
	nodeConf=$(dirname $(cd $(dirname $0) && pwd))
        sudo mkdir -p $nodeConf/log
        filename=$nodeConf/log/$MNAME"_"$(date +%Y%m%d_%H%M%S).txt
	sudo netstat -anp|grep -w $PORT >$filename
	echo "------">>$filename
	sudo route -n>>$filename 
	echo "------">>$filename
	docker exec $MNAME ss -anpt|grep -w ':'$PORT>>$filename
	echo "------">>$filename 
	sudo ifconfig>>$filename
	echo "------">>$filename
	docker exec $MNAME ifconfig>>$filename
	echo "------">>$filename
	docker exec $MNAME nc -v -w 5 -z ${NETADDR}.${shortname} $PORT 2>>$filename
	echo "------">>$filename
	docker exec $MNAME route -n>>$filename
	echo "------">>$filename
	sudo arp -a>>$filename

fi
