#!/bin/bash

#10.1.20.1/24
bridgeNet=$1
#10.1.20.1
bridgeIp=$2
#255.255.255.0
mask=$3
ostype=$4

brigeName=bridge0

bridgeInfo=$(ifconfig $brigeName)


if [ -z "${bridgeInfo}"  ]
then
	sudo brctl addbr $brigeName
	sudo ip addr add $bridgeNet dev $brigeName
	sudo ip link set dev $brigeName up
else
	currBridgeIp=''
	currMask=''
	if [ $ostype = 'ubuntu' ]
	then
		currBridgeIp=`ifconfig $brigeName|grep inet|grep -v inet6|awk '{print $2}'|awk -F : '{print $2}'`
		currMask=`ifconfig $brigeName|grep inet|grep -v inet6|awk '{print $4}'|awk -F : '{print $2}'`
	fi
	if [ $ostype = 'centos' -o $ostype = 'redhat' ]
	then
		currBridgeIp=`ifconfig $brigeName|grep inet|grep -v inet6|awk '{print $2}'`
		currMask=`ifconfig $brigeName|grep inet|grep -v inet6|awk '{print $4}'`
	fi
	if [ ${currBridgeIp} != ${bridgeIp} -o ${mask} != ${currMask}  ]
	then
		sudo ip link del $brigeName
		sudo brctl addbr $brigeName
		sudo ip addr add $bridgeNet dev $brigeName
		sudo ip link set dev $brigeName up
	fi
fi

numC=`cat /etc/os-release | grep '^NAME=' |grep -c CentOS`
if  [ ${numC} -gt 0 ];
then
echo " the os is CentOS!"
sudo systemctl restart docker
else
sudo service docker restart
fi

echo "OK"

