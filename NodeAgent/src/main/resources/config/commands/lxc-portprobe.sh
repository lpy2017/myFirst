#!/bin/bash
PORT=$1
APP=$2
FLAG=$3
if [ -n "${PORT}" ]; then
		if [ -n "${FLAG}" ]; then
    		RESULT=$(lxc-netstat -n ${APP} -${FLAG}n | grep $PORT)
    	else
    		RESULT=$(lxc-netstat -n ${APP} -lnp | grep $PORT)
    	fi
fi
echo ${RESULT}
