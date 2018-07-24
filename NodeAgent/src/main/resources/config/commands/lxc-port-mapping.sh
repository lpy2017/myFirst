#!/bin/bash
HOST_IP=$1
HOST_PORT=$2
LXC_IP=$3
LXC_PORT=$4
sourceIp=$5
protocol=$6

#selct=""
if [ -z $protocol ]
then
        protocol="tcp"
fi
#selct=$selct |grep "$protocol"

if [ $sourceIp -eq -1 ]
then
	sourceIp="0.0.0.0/0"
   #else
   #	selct=$selct |grep "$sourceIp"
   #	sourceIp=" -s "$sourceIp
fi

if [ "x${HOST_IP}" != "x" ] && [ "x${HOST_PORT}" != "x" ] && [ "x${LXC_IP}" != "x" ] && [ "x${LXC_PORT}" != "x" ]
then

    #check if exist mapping
    cnt=$(sudo iptables -t nat -L -n  |grep "${HOST_PORT}" |grep "${LXC_IP}:${LXC_PORT}" |grep "$sourceIp" |grep "$protocol" |wc -l)

    if [ $cnt -gt 0 ] ; then
	echo TRUE
	exit 0
    else

	sudo iptables -t nat -A PREROUTING -d ${HOST_IP} -s $sourceIp -p $protocol --dport ${HOST_PORT} -j DNAT --to-destination ${LXC_IP}:${LXC_PORT}

	#sudo iptables -t nat -A POSTROUTING -s ${LXC_IP} -p $protocol --dport ${LXC_PORT} -j SNAT --to ${HOST_IP}
    
	sudo iptables -t nat -A OUTPUT -d ${HOST_IP} -s $sourceIp -p $protocol --dport ${HOST_PORT}  -j DNAT --to ${LXC_IP}:${LXC_PORT}

	echo TRUE
    fi
 
 else
    echo FASLE
 fi
