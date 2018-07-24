#!/bin/bash
#容器启动后创建网络

source /etc/profile

cName=$1
cIp=$2
profile=$3



re=`sudo -E calicoctl ipam info  $cIp`
# No attributes defined for 192.168.1.14
# IP 192.168.1.1 is not currently assigned
if [[ $re =~ 'No' ]] && [[ $re =~ 'attributes' ]]
then
        echo "ip need release"
        sudo -E calicoctl ipam release $cIp
                #Address successfully released
fi


isExist=$(sudo -E calicoctl endpoint show --detailed|grep -w ${cIp} |awk '{print$6}')
if [ "${isExist}" != "" ] ; then
   # workloadID=${isExist}|awk '{print$6}'
    sudo -E calicoctl container remove ${isExist}
    #${cName}
fi

sudo -E calicoctl container add ${cName} ${cIp}
sudo -E calicoctl container ${cName} profile append ${profile}

