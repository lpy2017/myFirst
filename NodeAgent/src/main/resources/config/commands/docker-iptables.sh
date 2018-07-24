#!/bin/bash
#sudo service firewalld stop

sudo systemctl stop firewalld.service
sudo iptables -F