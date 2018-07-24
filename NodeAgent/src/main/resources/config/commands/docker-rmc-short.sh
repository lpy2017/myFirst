#!/bin/bash
containerName=$1
count=`sudo docker inspect --format="{{.Name}}" $containerName 2>&1 |wc -l`
if [[ $count -gt 1 ]]
then
	echo "success"
	exit 1
fi
result=`sudo docker rm -f  $containerName 2>&1`
echo $result
#echo "success"
count=`sudo docker inspect --format="{{.Name}}" $containerName 2>&1 |wc -l`
if [[ $count -gt 1 ]]
then
        echo "success"
        exit 1

fi
echo "delete rm failed!"

