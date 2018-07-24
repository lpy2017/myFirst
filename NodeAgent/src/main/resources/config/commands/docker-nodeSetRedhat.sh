#!/bin/bash

#自定义仓库ip地址
ip=$1
#自定义仓库机器域名
registryDormain=$2
#仓库端口号
port=$3

conf=/usr/lib/systemd/system/docker.service

count=$(sudo grep -c -o "$ip    $registryDormain" /etc/hosts )
if [ $count -eq 0 ]
then
sudo echo "$ip    $registryDormain" >> /etc/hosts
fi


count=$(sudo grep -c -o ".*--insecure-registry.*"   ${conf})

if [ $count -eq 0 ]
then
sudo sed -i 's#\(ExecStart.*-b=bridge0\).*\(-H.*\)#\1 '--insecure-registry=${registryDormain}:${port}' \2#g' ${conf}
else
sudo sed -i 's#\(ExecStart.*--insecure-registry=\).*\(-H.*\)#\1'${registryDormain}:${port}' \2#g' ${conf}
fi

sudo systemctl daemon-reload

sleep 1s
numC=`cat /etc/os-release | grep '^NAME=' |grep -c CentOS`
if  [ ${numC} -gt 0 ];
then
echo " the os is CentOS!"
sudo systemctl restart docker
else
sudo service docker restart
fi






