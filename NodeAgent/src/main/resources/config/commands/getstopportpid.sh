#!/bin/bash
PORT=$1
if [ -n "${PORT}" ]; then
    		RESULT=$(ps -fe|grep ${PORT}|grep -v grep)
fi
echo ${RESULT}|awk {'print$2'}
