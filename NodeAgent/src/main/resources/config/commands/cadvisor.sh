#!/bin/bash
currdir=$(cd `dirname $0`;pwd)
cd $currdir/..
file="config.properties"
if [[ -f $file ]];then
	echo "file ok" 
else
	echo "error:file not exists"
	exit 1;
fi
user=`cat $file | grep registryaddr | cut -d '=' -f2|awk -F "[://@]" '{print $4}'`
passwd=`cat $file | grep registryaddr | cut -d '=' -f2|awk -F "[://@]" '{print $5}'`
url=`cat $file | grep registryaddr | cut -d '=' -f2|awk -F "[://@]" '{print $6}'`
port=`cat $file | grep registryaddr | cut -d '=' -f2|awk -F "[://@]" '{print $7}'`

registry=$url":"$port
if [[ -z  $registry  ]];then 
#	echo "no param for registry"
	registry="registry.paas:443"
fi
registryUser=$user
if [[ -z $registryUser ]];then
#	echo "no param for registryUser"
	registryUser="admin"
fi
registryPwd=$passwd
if [[ -z $registryPwd ]];then
#	echo "no param for registryPwd"
	registryPwd="123456"
fi

echo $registryUser:$registryPwd@$registry

imageNum=`sudo docker images |grep $registry/google/cadvisor |wc -l`
if [ $imageNum == 0 ];then
	echo "no image ,start to pull"
	dockerlogin=`sudo docker login -u $registryUser -p $registryPwd  $registry`
	if [[ $dockerlogin =~ "Succeeded" ]];then
		echo "login ok"
		dockerpull=`sudo docker pull $registry/google/cadvisor:latest`
		echo $dockerpull
		if [[  $dockerpull =~ "error"   ]];then 
			echo "pull image error"
			exit 1;
		fi
		echo "pullimage ok"
	else
		echo "login error"
		exit 1;
	fi
else

echo "image ok"
fi

#check container
containerExist=`sudo docker inspect --format='{{.Name}}' cadvisor 2>&1|grep -w  /cadvisor|wc -l`
if [[ $containerExist  == "0" ]];then
echo "no container"
result=`sudo docker run --privileged   --volume=/:/rootfs:ro --volume=/var/run:/var/run:rw  --volume=/sys:/sys:ro  --volume=/var/lib/docker/:/var/lib/docker:ro --net=host --detach=true  --name=cadvisor   $registry/google/cadvisor:latest`
echo $result
fi

#check container status
containerStatus=`sudo docker inspect --format='{{.State.Status}}' cadvisor`
if [[ $containerStatus == "running"   ]];then
	echo "container is running"

else
	echo "container is not running"
	sudo docker start cadvisor
fi

echo "ok"

