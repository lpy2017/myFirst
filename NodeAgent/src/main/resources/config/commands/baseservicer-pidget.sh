#!/bin/bash
CNAME=$1 
FLAG=$2
pid=$(docker exec $CNAME ps -ef |grep $flag | grep -v 'grep'| awk {'print $2'})
echo ${pid}





