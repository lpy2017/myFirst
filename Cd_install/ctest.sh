#!/bin/sh
#looptimes 循环次数 time  每次循环时间
path=$(cd "`dirname "$0"`"; pwd)
LOOPTIMES=30
TIME=20
curr_time=`date +%Y%m%d%H%M`

while true 
do
	LOOPTIMES=$((LOOPTIMES-1))
	echo "looptimes=$LOOPTIMES"
	if [[ $LOOPTIMES == 0 ]]
	then
        	echo "delopy timeout!!!"
        	exit 1
	fi
	sleep $TIME

	echo "curl  http://${uihost}:5085/cloudui/app/pages/login.html -o $path/curltmp"
	curl  http://${uihost}:5085/cloudui/app/pages/login.html -o $path/curltmp
	echo "======================"
	result=`cat $path/curltmp`
	echo $result
	echo "======================"
	
	if [[  -z $result ]]
	then
        	echo "connect failed"
	else
		echo "">$path/curltmp
		echo "connect success"
		#salt '*' test.ping
        	salt 'chengyang-4.digitalchina.com' cmd.script salt://scripts/testCd.bat $curr_time
		break
	fi
	echo "">$path/curltmp
done