#!/bin/bash
container=$1
sourcePath=$2
destPath=$3
parentPath=$(dirname $destPath)
echo "rootPath:"$parentPath
r1=`sudo docker exec  $container mkdir -p $parentPath 2>&1`
echo $r1
if [[ $r1 =~ 'rror' ]]
then
	exit -1;
fi
r2=`sudo docker cp $sourcePath $container:/$parentPath 2>&1`
echo $r2
if [[ $r2 =~ 'rror' ]]
then
        exit -1;
fi

echo "success"
