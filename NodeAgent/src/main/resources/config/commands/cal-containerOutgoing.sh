#!/bin/bash
AorD=$1
cIp=$2
hostIp=$3
outIP=$4
protocol=$5
outPort=$6


temport=''
temgrep=''
if [ "$5" != "" ] ;then
    temport=${temport}' -p '$protocol
fi

if [ "$6" != "" ];then
    temgrep=${temgrep} |grep ${outPort}
    temport=${temport}' --dport '${outPort}
fi

#check if exist mapping
cnt=$(sudo iptables -t nat -L -n |grep ${outIP}|grep MASQUERADE|awk {'print$4,$7'}|grep ${cIp} ${temgrep} |wc -l)

#if [ ${cnt} == 0 -a ${AorD} == "A" ] || [ ${cnt} != 0 -a ${AorD} == "D" ] 
#    then
	#iptables -t nat -${AorD} POSTROUTING -s ${cIp} -d ${outIP} -o ${eth0Name} -j SNAT --to-source ${hostIp}

	sudo iptables -t nat  -${AorD} POSTROUTING -s ${cIp} -d ${outIP} ${temport} -j MASQUERADE

#else
#	echo "exist"
#	exit 0
#fi

#check whether mapping successfully
afterCnt=$(sudo iptables -t nat -L -n |grep ${outIP}|grep MASQUERADE|awk {'print$4'}|grep ${cIp} ${temgrep}|wc -l)
if [ ${afterCnt} == 1 -a ${AorD} == "A" ] || [ ${afterCnt} == 0 -a ${AorD} == "D" ]; then
	echo "ok"
fi

###################################
#if [ "$5" != "" ] ; then
#    profile=$5
#    CIDRS=$6
#    sudo -E calicoctl profile add ${profile}
#    sudo -E calicoctl profile ${profile} rule remove inbound allow from tag ${profile}
#    sudo -E calicoctl profile ${profile} rule remove outbound allow
#    sudo -E calicoctl profile ${profile} rule add outbound allow to cidr ${CIDRS}
#fi

