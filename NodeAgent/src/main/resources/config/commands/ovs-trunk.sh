#!/bin/bash
op=$1
remoteIp=$2

if [ "$1" == "create" ] ; then
	if [ "$2" == "" ] ; then 
		echo "failed: no remote ip. usege: bash trunk.sh op remoteIp"
		exit 0
	else
		sudo ovs-vsctl add-port ovs0 gre${remoteIp} -- set interface gre${remoteIp} type=gre options:remote_ip=${remoteIp}
		echo "OK"	
	fi
elif [ "$1" == "delete" ] ; then
	if [ "$2" == "" ] ; then
		echo "failed: no remote ip. usege: bash trunk.sh op remoteIp"
		exit 0
	else
		sudo ovs-vsctl del-port gre${remoteIp}
		echo "OK"
	fi
else
	echo "failed: unknow operate. usege: create or delete."
fi
