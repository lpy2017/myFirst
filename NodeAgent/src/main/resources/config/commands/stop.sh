#!/bin/bash
SIGNAL=$2
PID=$1
if [ -n "${PID}" ]; then
    if [ -n "${SIGNAL}" ]; then
        kill -${SIGNAL} ${PID}
    else 
        kill ${PID}
    fi
    echo TRUE
else
    echo FALSE
fi
