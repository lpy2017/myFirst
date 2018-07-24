#!/bin/bash
br0Ip=$1
if [ "$1" == "" ] ; then
	br0Ip="172.17.0.1/16"
fi

if sudo brctl show|awk '{ print $1 }'|grep -q "^br0$" ; then
	echo "br0 is exist, skip to add br0."
else
	sudo brctl addbr br0
fi
	sudo ifconfig br0 ${br0Ip} up
	#sudo iptables -t nat  -A POSTROUTING -s ${br0Ip} ! -o br0 -j MASQUERADE
	echo "OK"

