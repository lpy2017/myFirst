#!/bin/bash
CNAME=$1
HTTPPORT=$2
FLAG=$3
pid=$(sudo docker inspect -f '{{.State.Pid}}' $CNAME)
#创建网络命名空间
sudo mkdir -p /var/run/netns
sudo ln -s /proc/${pid}/ns/net /var/run/netns/${pid}
tmp=`sudo ip netns exec $pid ss -lnpt|grep -w $HTTPPORT`
if [ -n "$tmp" ]
	then
	#	echo $tmp|sed 's/.*"java",\([^,]*\).*/\1/'
	#        echo $tmp|sed 's/.*"'${FLAG}'",\([^,]*\).*/\1/'
	echo $tmp|sed 's/.*".*",\([^,]*\).*/\1/'
else
		echo '-1'
fi