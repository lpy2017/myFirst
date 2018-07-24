#!/bin/bash
CHPARA=$1
CHPATH=$2
chown -Rv ${CHPARA} ${CHPATH} >${CHPATH}/tmp.log
echo ready