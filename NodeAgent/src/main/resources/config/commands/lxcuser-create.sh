#!/bin/bash
PARA=$1
PPATH=$2
CHPARA=$3
CHPATH=$4
PARAPW=$5
SPATH=$6
echo ${PARA} >> ${PPATH}
chown -Rv ${CHPARA} ${CHPATH} >${CHPATH}/tmp.log
echo ${PARAPW} >> ${SPATH}
echo ready