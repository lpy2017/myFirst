#!/bin/bash

root=$1
passwd=$2
email=$3
addr=$4
 
docker login -u $root -p $passwd -e $email $addr
