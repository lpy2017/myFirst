#!/bin/bash


#the first param mast be container name
for str in $*
do
command=$command" "$str
done
sudo docker exec $command 2>&1
