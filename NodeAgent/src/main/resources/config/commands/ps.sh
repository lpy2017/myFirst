#!/bin/bash
CON=$1
if [ "x${CON}" != "x" ]; then
    exec ps -eopid,cmd | grep $CON
else
    ps -eopid,cmd
fi
