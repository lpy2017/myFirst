#!/bin/bash
HOST_IP=$1
HOST_PORT=$2
LXC_IP=$3
LXC_PORT=$4
sourceIp=$5
protocol=$6
if [[ -z $protocol ]]
then
        protocol="tcp"
fi

if [ $sourceIp -eq -1 ]
then
	sourceIp=""
else
	sourceIp=" -s "$sourceIp
fi

if [ "x${HOST_IP}" != "x" ] && [ "x${HOST_PORT}" != "x" ] && [ "x${LXC_IP}" != "x" ] && [ "x${LXC_PORT}" != "x" ]
then
    sudo iptables -t nat -D PREROUTING -d ${HOST_IP} $sourceIp -p $protocol --dport ${HOST_PORT} -j DNAT --to-destination ${LXC_IP}:${LXC_PORT}

    #sudo iptables -t nat -D POSTROUTING -s ${LXC_IP} -p $protocol --dport ${LXC_PORT} -j SNAT --to ${HOST_IP}

    sudo iptables -t nat -D OUTPUT -d ${HOST_IP} $sourceIp -p $protocol --dport ${HOST_PORT}  -j DNAT --to ${LXC_IP}:${LXC_PORT}

    echo TRUE
 else
    echo FASLE
 fi
