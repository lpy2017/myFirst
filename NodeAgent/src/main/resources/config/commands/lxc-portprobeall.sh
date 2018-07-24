#!/bin/bash
ports=$1
OLD_IFS=$IFS
IFS=':'
portarg=($ports)
echo ${portarg[0]}
for port in ${portarg[@]}
do
echo $port
IFS='#'
portnname=($port)
COUNT=$(lxc-netstat -n ${portnname[1]} -anp| grep ESTABLISHED | grep -wE  ${portnname[0]}| wc -l)
echo  ${portnname[0]}   $COUNT
IFS=':'
done
IFS=$OLD_IFS


#PORT=$1
#APP=$2
#if [ -n "${PORT}" -a ${PORT} -gt 0 -a ${PORT} -le 65535 ]; then
#    COUNT=$(lxc-netstat -n ${APP} -anp| grep ":${PORT} " | grep ESTABLISHED | wc -l)
#    echo $COUNT
#else
#    echo 0
#fi
