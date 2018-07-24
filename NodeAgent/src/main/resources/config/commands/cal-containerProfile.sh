#!/bin/bash
#容器加入一级负载网络

cName=$1
appendOrRemove=$2
profile=$3

source /etc/profile

sudo -E calicoctl container ${cName} profile ${appendOrRemove} ${profile}
