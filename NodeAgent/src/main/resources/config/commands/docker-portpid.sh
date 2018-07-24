#!/bin/bash
PORT=$1
CNAME=$2
#RESULT=$(docker exec $CNAME netstat -lnp | grep $PORT |grep tcp)

RESULT=$(docker exec $CNAME netstat -lnp | grep :$PORT |grep tcp|awk {'print$7'}|cut -d / -f 1)

echo ${RESULT}
