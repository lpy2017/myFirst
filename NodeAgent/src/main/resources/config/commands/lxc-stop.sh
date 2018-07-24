#!/bin/bash
APP=$1
if  [ "x${APP}" != "x" ]
then
    lxc-stop -n ${APP}
 else
    echo FASLE
 fi