#!/bin/bash

num=`sudo docker ps -a|grep SCN |wc -l`
while [[ $num -ne 0   ]]
do
	sudo docker rm -f $(docker ps -a|grep SCN |awk '{print $1}')
	num=`sudo docker ps -a|grep SCN |wc -l`
done
echo "success"
