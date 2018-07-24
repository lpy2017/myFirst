#!/bin/bash

source /etc/profile

nodeName=`hostname`
sudo -E calicoctl node stop --force  
sudo -E calicoctl node remove --hostname=${nodeName} --remove-endpoints
docker rm calico-node

