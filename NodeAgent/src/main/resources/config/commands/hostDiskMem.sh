#!/bin/bash
data=`df -lm|awk '{print $2}'`
index=0
all=0
for block in $data
do
if [[ index -ne 0 ]];
then
	#echo ${block}
	all=$[all+block]
fi
index=$[index+1] 
done
memAll=`free -m |sed -n "2p"|awk '{print $2}'`
echo "$all,$memAll"
