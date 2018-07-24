#!/bin/bash
#容器启动后创建网络

source /etc/profile

profile=$1
addOrRemove=$2
protocol=$3
port=$4
lxcIP=$5
#sudo -E calicoctl profile ${profile} rule ${addOrRemove} inbound allow ${protocol} to ports ${port}  //无差别添加端口开放
`sudo -E calicoctl profile ${profile} rule show |grep -E 'allow.*cidr' |awk '{print$8,$6}' |grep ${port} |grep ${lxcIP} wc -l`



