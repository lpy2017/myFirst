#!/bin/bash
cname=$1
ports=$2
#echo $ports
docker exec $cname netstat -anpt|grep ESTABLISHED|grep -wE ${ports//:/|}|awk '{print$4}'|awk -F: '{print$2}'|sort -n|uniq -c
