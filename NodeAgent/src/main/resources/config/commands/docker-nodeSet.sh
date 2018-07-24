#!/bin/bash

#自定义仓库ip地址
ip=$1
#自定义仓库机器域名
registryDormain=$2
#仓库端口号
port=$3
 
count=$(sudo grep -c -o DOCKER_OPTS='"$DOCKER_OPTS --insecure-registry='${registryDormain}:${port}'"'  /etc/default/docker)

if [ $count -eq 0 ]
then
sudo echo DOCKER_OPTS='"$DOCKER_OPTS --insecure-registry='${registryDormain}:${port}'"' >> /etc/default/docker
fi

sudo sed -i /.*$registryDormain.*/d /etc/hosts
count=$(sudo grep -c -o "$ip    $registryDormain" /etc/hosts )
if [ $count -eq 0 ]
then
sudo echo "$ip    $registryDormain" >> /etc/hosts
fi

numC=`cat /etc/os-release | grep '^NAME=' |grep -c CentOS`
if  [ ${numC} -gt 0 ];
then
echo " the os is CentOS!"
sudo systemctl restart docker
else
sudo service docker restart
fi