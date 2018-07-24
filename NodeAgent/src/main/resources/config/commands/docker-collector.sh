#!/bin/bash
CNAME=$1

#pid=$(docker inspect -f '{{.State.Pid}}' $CNAME)
#info=`docker stats $CNAME|head -6|tail -1|xargs`
#echo $info

pid=$(docker inspect -f '{{.State.Pid}}' $CNAME)

if [ -n "$pid" ]
then
  if [ "$pid" = "0" ]
  then
   echo "Error:Current container "$CNAME "is not running"
 else
   #docker stats $CNAME|head -6|tail -1|xargs
   docker stats --no-stream $CNAME|tail -1
 fi

fi
