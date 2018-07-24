#!/bin/bash
HOST_IP=$1
LXC_IP=$2
OP_FLAG=$3
if [ "x${HOST_IP}" != "x" ] && [ "x${OP_FLAG}" != "x" ] && [ "x${LXC_IP}" != "x" ]
then

    #sudo iptables -t nat -${OP_FLAG} POSTROUTING -s ${LXC_IP}  -j SNAT --to ${HOST_IP}

    echo TRUE
 else
    echo FASLE
 fi
   
