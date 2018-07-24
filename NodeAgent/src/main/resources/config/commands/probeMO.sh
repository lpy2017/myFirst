#!/bin/bash
MOM_PORT=5137
OUT_PORT=65532
LOG_PORT=65531

osdesc=`lsb_release -i`
if [[ $osdesc =~ "Ubuntu" ]]
then
RES=`ss -lntp|grep -Ew ':'${OUT_PORT}'|:'${MOM_PORT}'|:'${LOG_PORT}|awk  '{print$4,$6}'`
OLD_IFS=$IFS
#echo $RES
IFS=$'\n'
for d in $RES
do
#[[ $d =~ $MOM_PORT ]] && echo "\$str contains this"
if [[ $d =~ $MOM_PORT ]]
then
echo "MOM:"`echo $d|sed 's/.*"java",\([^,]*\).*/\1/'`
elif [[ $d =~ $OUT_PORT ]]
then
echo "OUT:"`echo $d|sed 's/.*"java",\([^,]*\).*/\1/'`
elif [[ $d =~ $LOG_PORT ]]
then
echo "LOG:"`echo $d|sed 's/.*"java",\([^,]*\).*/\1/'`
fi
done
set IFS=$OLD_IFS


#not ubuntu
else
RES=`netstat -lntp|grep -Ew ${OUT_PORT}'|:'${MOM_PORT}'|:'${LOG_PORT}|awk  '{print$4,$7}'`
IFS=$'\n'
for d in $RES
do
if [[ $d =~ $MOM_PORT ]]
then
echo "MOM:"`echo $d|awk '{print$2}'|awk -F/ '{print$1}'`
elif [[ $d =~ $OUT_PORT ]]
then
echo "OUT:"`echo $d|awk '{print$2}'|awk -F/ '{print$1}'`
elif [[ $d =~ $LOG_PORT ]]
then
echo "LOG:"`echo $d|awk '{print$2}'|awk -F/ '{print$1}'`
fi
done
set IFS=$OLD_IFS
fi