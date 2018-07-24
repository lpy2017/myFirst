#!/bin/bash
pn=`ps -ef|grep -i "AppMaster.jar"|grep -v grep|awk '{print $2}'`
if [ -n "$pn" ];then
echo "killing AppMaster process,excute kill -9 $pn"

kill -9 $pn

fi
