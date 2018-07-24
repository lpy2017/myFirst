#!/bin/bash
HOST_IP=$1
LXC_IP=$2
LXC_PORT=$3
OP=$4

#check is port mapping muti-times
prertcnt=$(sudo iptables -t nat -L -n |grep ${LXC_PORT}|grep DNAT|awk {'print$5'}|grep ${HOST_IP} |wc -l)
outputcnt=$(sudo iptables -t nat -L -n |grep ${LXC_PORT}|grep DNAT|awk {'print$5'}|grep 0.0.0.0 |wc -l)

if [ $OP = 'A' ];then

	#check is iptables success
	isprert=$(sudo iptables -t nat -L -n|grep ${LXC_IP}:${LXC_PORT}|awk {'print$5'}|grep ${HOST_IP}|wc -l)
	isoutput=$(sudo iptables -t nat -L -n|grep ${LXC_IP}:${LXC_PORT}|awk {'print$5'}|grep 0.0.0.0|wc -l)



	if [ $isprert -ge 1 -a $isoutput -ge 1 ];then
		if [ $prertcnt = 1 -a $outputcnt = 1 ];then
			echo 0
		else
			if [ $prertcnt != 1 ];then
				echo precnt$prertcnt
			elif [ $prertcnt != 1 ];then
				echo outcnt$prertcnt
			fi
		fi
	else
		if [ $isprert -lt 1 ];then
			echo isprert$isprert
		elif [ $isoutput -lt 1 ];then
			echo isoutput$isoutput
		fi

	fi
else
	if [ $prertcnt = 0 -a $outputcnt = 0 ];then
		echo 0
	else
		if [ $prertcnt != 0 ];then
			echo precnt$prertcnt
		elif [ $prertcnt != 0 ];then
			echo outcnt$prertcnt
		fi
	fi
fi