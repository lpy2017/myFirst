#!/bin/bash
nodeName=`hostname`
nodeIp=$1
etcdUrl=$2
hostname=$(cat /etc/profile | grep "export HOSTNAME=")
etcd_authority=$(cat /etc/profile | grep "export ETCD_AUTHORITY=")
if [[ "${hostname}" == "" ]] ; then 
    sudo sh -c 'echo "export HOSTNAME="'${nodeName}' >> /etc/profile'
else
    sudo sed -i 's/export HOSTNAME=.*/export HOSTNAME='${nodeName}'/g' /etc/profile
fi
if [[ "${etcd_authority}" == "" ]] ; then 
    sudo sh -c 'echo "export ETCD_AUTHORITY="'${etcdUrl}' >> /etc/profile'
else
    sudo sed -i 's/export ETCD_AUTHORITY=.*/export ETCD_AUTHORITY='${etcdUrl}'/g' /etc/profile
fi
source /etc/profile
 
caltest=`docker ps -a |grep 'calico-node' |awk '{print $2}'`
if [[ $caltest != "" ]] ; then
    sudo docker rm -f calico-node
fi
rs=`sudo -E calicoctl node --ip=${nodeIp} --node-image=calico/node`

if [[ $rs =~ "running with id" ]] ; then
    echo "OK"
fi

