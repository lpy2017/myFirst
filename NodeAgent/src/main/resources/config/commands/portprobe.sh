#!/bin/bash
PORT=$1
FLAG=$2
if [ -n "${PORT}" ]; then
		if [ -n "${FLAG}" ]; then
    		RESULT=$(netstat -${FLAG}nt | grep -w $PORT | grep -wE "LISTEN|ESTABLISHED")
    	else
    		RESULT=$(netstat -lnt | grep -w $PORT)
    	fi
fi
echo ${RESULT}