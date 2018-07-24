#!/bin/bash
APP=$1
if [ "x${APP}" != "x" ]
then
    lxc-netstat -n ${APP}
 else
    echo FASLE
 fi
   