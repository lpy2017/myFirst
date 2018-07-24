#!/bin/bash
#ÈÝÆ÷Ð¶ÔØÇ°É¾³ýÍøÂç
cName=$1
ip=$2
source /etc/profile

sudo -E calicoctl container remove ${cName}

if [[ -z $ip ]]
then
        exit -1
fi
re=`sudo -E calicoctl ipam info  $ip`
# No attributes defined for 192.168.1.14
# IP 192.168.1.1 is not currently assigned
if [[ $re =~ 'No' ]] && [[ $re =~ 'attributes' ]]
then
        echo "ip need release"
        sudo -E calicoctl ipam release $ip
		#Address successfully released
fi


