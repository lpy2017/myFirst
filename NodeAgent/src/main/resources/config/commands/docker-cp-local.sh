#!/bin/bash
container=$1
sourcePath=$2
destPath=$3
parentPath=$(dirname $destPath)
sudo mkdir -p $parentPath
sudo docker cp $container:$sourcePath $parentPath
echo "success"
