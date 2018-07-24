#!/bin/bash
PORT=$1
RESULT=$(sudo netstat -lnp | grep :$PORT |grep tcp)
echo ${RESULT}|awk {'print$7'}
