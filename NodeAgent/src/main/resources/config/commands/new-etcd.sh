#!/bin/bash
usage='Usage:
  '$0' <etcdName> [etcdClusterList]
  i.e. etcdClusterList="etcd0=http://192.168.209.137:2380,etcd1=http://192.168.209.138:2380"'
etcdName=$1
etcdIp=$2
etcdClusterList=$3
if [ "${etcdClusterList}" == "" ]; then 
	etcdClusterIsNew="new"
else
        etcdClusterIsNew="existing"
        len=${#etcdClusterList}
        len=`expr ${len} - 1`
        if [ "${etcdClusterList:${len}}" != "," ] ; then
                etcdClusterList=${etcdClusterList}","
        fi
        node0=$(echo "${etcdClusterList}" | awk -F "80," '{ print $1 }'| awk -F "=" '{print $2}')
        curl "${node0}79/v2/members" -XPOST \
        -H "Content-Type: application/json" -d '{"peerURLs":["http://'"${etcdIp}"':2380"]}' 
fi
sudo docker run -d -v /usr/share/ca-certificates/:/etc/ssl/certs -p 4001:4001 -p 2380:2380 -p 2379:2379 \
--restart=always \
--name etcd etcd:3.0.1 \
 -name ${etcdName} \
 -advertise-client-urls http://${etcdIp}:2379,http://${etcdIp}:4001 \
 -listen-client-urls http://0.0.0.0:2379,http://0.0.0.0:4001 \
 -initial-advertise-peer-urls http://${etcdIp}:2380 \
 -listen-peer-urls http://0.0.0.0:2380 \
 -initial-cluster-token etcd-cluster-1 \
 -initial-cluster ${etcdName}=http://${etcdIp}:2380,${etcdClusterList} \
 -initial-cluster-state ${etcdClusterIsNew}
