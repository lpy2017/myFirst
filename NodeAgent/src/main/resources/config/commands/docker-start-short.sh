#!/bin/bash
CONTAINER=$1
sudo docker start $CONTAINER
start_result=`sudo docker inspect --format="{{.State.Status}}" $CONTAINER`
if [[ $start_result == 'running' ]]
then
echo "success"
else
echo "start error"
fi
