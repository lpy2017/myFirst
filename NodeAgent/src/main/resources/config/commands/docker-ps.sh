#!/bin/bash
CNAME=$1
CSTATUS=$2

if [ '0' = "$CSTATUS" ]
then
	echo `docker ps|grep -w $CNAME`
else
# get the exited or create.
	 res=`docker ps -af status=exited|grep -w  $CNAME`
	 if [ -z "$res" ]
		then
#check if the docker is start
			echo `docker ps|grep -w $CNAME`
		else
			echo $res
		fi
fi




