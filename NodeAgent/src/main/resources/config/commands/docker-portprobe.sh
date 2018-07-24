#!/bin/bash
CNAME=$3
PORT=$1
STOPPORT=$2
flag=$4

if [ "l" = "$flag" ]
then
RES=`docker exec $CNAME ss -${flag}npt|grep -wE ':'$PORT'|'$STOPPORT|awk '{print$4,$6}'`
else
RES=`docker exec $CNAME ss -${flag}npt|grep -wE ':'$PORT'|'$STOPPORT|grep -wE 'LISTEN|ESTAB' |awk '{print$4,$6}'`
fi
OLD_IFS=$IFS
IFS=$'\n'
for d in $RES
do
echo $d
done
IFS=$OLD_IFS



