#!/bin/bash
MODELPATH=$1
MNAME=$2
MIP=$3

lxc-create -f ${MODELPATH} -n ${MNAME}

sed -i  's/0.0.0.0/'${MIP}'/' /var/lib/lxc/${MNAME}/config 

