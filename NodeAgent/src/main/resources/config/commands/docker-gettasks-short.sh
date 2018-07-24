#!/bin/bash
list=$(sudo docker ps -a|grep SCN |awk '{print $1}')
if [[ -z $list ]]
then
	exit 1;
fi

sudo docker inspect --format="{{.Name}}" $list |cut -d \/ -f 2 
