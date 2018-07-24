#!/bin/bash
PORT=$1
APP=$2
if [ -n "${PORT}" -a ${PORT} -gt 0 -a ${PORT} -le 65535 ]; then
    COUNT=$(lxc-netstat -n ${APP} -anp| grep ":${PORT} " | grep ESTABLISHED | wc -l)
    echo $COUNT
else
    echo 0
fi
   