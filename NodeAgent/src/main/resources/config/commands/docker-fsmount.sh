#!/bin/sh
type=$1
address=$2
path=$3

sudo mount -t $type $address $path

echo "OK"
