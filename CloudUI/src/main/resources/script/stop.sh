#!/bin/bash

pn=`ps -ef|grep -i "cloudui.jar"|grep -v grep|awk '{print $2}'`
if [ -n "$pn" ];then
echo "killing Cloudui process,excute kill -9 $pn"

kill -9 $pn

fi
