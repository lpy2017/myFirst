#!/bin/bash
if [[ -z $2 ]]
then
	echo "error:need two params..."
	exit -1
fi
sourceFile=$1
linkFile=$2
if [[ ! -f $sourceFile ]]
then
	echo "error:cannot get the sourceFile:$sourceFile"
	exit -1
fi 
echo "create soft link from  $1 to $2"
ln -s $1 $2
echo "link logfile success"
