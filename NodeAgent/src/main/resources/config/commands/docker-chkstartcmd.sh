#!/bin/bash
cname=$1
dir=$2
file=$3

count=$(docker exec $cname ls -l $dir|grep -wE ${file}$|wc -l)

if [ $count -eq 1 ]
then
echo TRUE
else
echo FALSE
fi
