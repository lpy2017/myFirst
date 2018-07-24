#!/bin/bash
registry=$1
userName=$2
password=$3
imageName=$4
image=$registry"/"$imageName
if [[ -z $5 ]]
then
	ifExist=`docker images |awk '{print $1":"$2}'|grep $image |wc -l`
	if [[ $ifExist -gt 0 ]];
	then 
		echo "image in local" 
		echo "success"
	exit 1;
	fi
else
	echo "always update image!"
fi

dockerVersion=`sudo docker version|grep Version | sed -n '1p'|cut -d \. -f 2`
echo $dockerVersion
if [[ $dockerVersion -le 10 ]]
then
        EMAIL=" -e admin@admin.com"
fi
loginResult=`sudo docker login -u $userName -p $password $EMAIL  $registry 2>&1`
lrc=`echo $loginResult |grep ucceed|wc -l`

if [[ $lrc -eq 1 ]]
then
	loginSuccess=true
else
	errorMsg=$loginResult
	echo $errorMsg
	exit 1;
fi

sudo docker pull $image 2>&1

ifExist=`docker images |awk '{print $1":"$2}'|grep $image |wc -l`
if [[ $ifExist -gt 0 ]];
then
 echo "success"
else
echo imageerror
fi



