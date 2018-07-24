#!/bin/bash
cname=$1
cid=` docker ps -a --no-trunc|grep -w  ${cname}|awk '{print $1}'`

echo $cid
