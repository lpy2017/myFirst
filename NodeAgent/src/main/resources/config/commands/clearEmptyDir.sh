#!/bin/bash
basePath=$1
if [[ -z $1 ]]
then
basePath=$(cd `dirname $0`;pwd)
fi
echo $basePath
if [[ -d $basePath ]]
then
	echo "is a real path,continue....."
	else
	echo "not a dir,exit"
	exit 0
fi

for d in $(ls $basePath)
do
	tempPath=$basePath"/"$d
	if [[ -d $tempPath ]]
	then
		fileNum=$(ls $tempPath|wc -l)	
		if [[ $fileNum -eq 0 ]]
		then
			echo "del "$tempPath
			rm -rf $tempPath
		fi
	else
		echo $tempPath" is a file"
	fi


done
