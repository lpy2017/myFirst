#!/bin/bash
#��������һ����������

cName=$1
appendOrRemove=$2
profile=$3

source /etc/profile

sudo -E calicoctl container ${cName} profile ${appendOrRemove} ${profile}
