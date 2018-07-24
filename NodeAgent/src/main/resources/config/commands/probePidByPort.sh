#!/bin/bash
PORT=$1
if [[ -z $PORT ]]
then
	exit -1
fi
sudo netstat -anp|grep -w $PORT |grep  LISTEN|awk '{print$7}'  |cut -d / -f 1
