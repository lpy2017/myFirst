#!/bin/bash
cName=$1
ipType=$2

pid=$(docker inspect -f '{{.State.Pid}}' $cName)
if [ "$2" == "all" ] ; then
	if [ "$5" = "" ] ; then
		echo "usege: bash cnetwork.sh  cName ipType cIp [br0Ip/vlantag] [cip] [br0Ip/vlantag]"
		echo "such as bash cnetwork.sh test1 all   172.17.0.2/16  172.17.0.1  10.1.2.2/24 100"
		echo "such as bash cnetwork.sh test1 outer 172.17.0.2/16 172.17.0.1"
		echo "such as bash cnetwork.sh test1 inner 10.1.2.2/24 100"
		exit 1
	elif [ "$6" == "" ] ; then
		innerIp=$5
		sudo pipework ovs0 -i eth1 ${cName} ${innerIp}
	else
		innerIp=$5
		vlantag=$6
		sudo pipework ovs0 -i eth1 ${cName} ${innerIp}@${vlantag}
	fi
		cIp=$3
		br0Ip=$4
		sudo pipework br0 -i eth0 ${cName} ${cIp}@${br0Ip}
		echo "OK"

elif [ "$2" == "outer" ] ; then
	if [ "$4" == "" ] ; then
		echo "usege: bash cnetwork.sh cName ipType cIp [br0Ip/vlantag]"
		echo "such as bash cnetwork.sh test1 outer 172.17.0.2/16 172.17.0.1"
		echo "such as bash cnetwork.sh test1 outer 10.1.2.2/24 100"
		exit 1
	else
		cIp=$3
		br0Ip=$4
		sudo pipework br0 -i eth0 ${cName} ${cIp}@${br0Ip}
	fi
	echo "OK"

elif [ "$2" == "inner" ] ; then
	if [ "$3" = "" ] ; then
		echo "usege: bash cnetwork.sh cName ipType cIp [br0Ip/vlantag]"
		echo "such as bash cnetwork.sh test1 outer 172.17.0.2/16 172.17.0.1"
		echo "such as bash cnetwork.sh test1 inner 10.1.2.2/24 100"
		exit 1
	elif [ "$4" == "" ] ; then
		cIp=$3
		sudo pipework ovs0 -i eth1 ${cName} ${cIp}
	else
		cIp=$3
		vlantag=$4
		sudo pipework ovs0 -i eth1 ${cName} ${cIp} @${vlantag}
	fi
	echo "OK"
	
else
	echo "error"
	echo "usege: bash cnetwork.sh cName ipType cIp [br0Ip/vlantag]"
	echo "such as bash cnetwork.sh test1 outer 172.17.0.2/16 172.17.0.1"
	echo "such as bash cnetwork.sh test1 inner 10.1.2.2/24 100"
fi
