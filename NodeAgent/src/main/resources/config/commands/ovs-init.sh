#!/bin/bash
date
isSudoer=""
if [ "$1" == "" ] ; then
    isSudoer=sudo
fi
$isSudoer bash /usr/share/openvswitch/scripts/ovs-ctl start

if $isSudoer ovs-vsctl list-br|grep -q "^ovs0$" ; then 
	echo "ovs0 is exist,  delete it."
	$isSudoer ovs-vsctl del-br ovs0
fi
$isSudoer ovs-vsctl add-br ovs0
$isSudoer ovs-vsctl set br ovs0 stp_enable=true
echo "add ovs config to rc.local"
ostype=`$isSudoer cat /etc/os-release |grep 'ID'|awk {print}`
if [[ "${ostype}" =~ "ubuntu" ]];
    then
    startFile="/etc/rc.local"
elif [[ "${ostype}" =~ "centos" ]] || [[ "${ostype}" =~ "redhat" ]];
    then
    startFile="/etc/rc.d/rc.local"
else
    echo "unknow os"
fi
$isSudoer chmod +x $startFile
$isSudoer sed -i "/ovs-init.sh/d" $startFile
$isSudoer sed -i "/exit 0/d" $startFile
$isSudoer sh -c  'echo "nohup bash '`cd $(dirname $0)&& pwd`'/ovs-init.sh'' notSudoer  > /etc/ovs-init.log &" >> '$startFile
$isSudoer sh -c  'echo "exit 0" >> '$startFile

echo "OK"
