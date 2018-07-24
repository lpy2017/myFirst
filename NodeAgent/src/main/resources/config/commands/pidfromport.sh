#!/bin/bash
PORT=$1
FLAG=$2
if [ -n "${PORT}" ]; then
		if [ -n "${FLAG}" ]; then
    		RESULT=$(sudo netstat -${FLAG}ntp | grep -w :$PORT | grep -wE "LISTEN|ESTABLISHED"|awk {'print$7'}|cut -d / -f 1)
    	else
    		RESULT=$(sudo netstat -lntp | grep -w :$PORT|awk {'print$7'}|cut -d / -f 1)
    	fi
fi
echo ${RESULT}
