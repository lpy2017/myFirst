#!/bin/bash

pn=`ps -ef|grep "jar Adapter"|grep -v "grep"|awk '{print $2}'`
if [ -n "$pn" ];then
echo "killing Adapter process,excute kill -9 $pn"

kill -9 $pn

fi
