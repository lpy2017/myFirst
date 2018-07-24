#!/bin/bash
TARGET=$1
if [ -d ${TARGET} ]; then
    rm -r ${TARGET}
    echo TRUE
elif [ -f ${TARGET} ]; then
    rm ${TARGET}
    echo TRUE
else
    echo FALSE
fi
