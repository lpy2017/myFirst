#!/bin/bash

repoaddr=$1
masterip=$2

sed -i "s|baseurl=.*|baseurl=$repoaddr/|g"  ./localrepo.repo

mkdir -p /etc/yum.repos.d/repobak
mv /etc/yum.repos.d/* /etc/yum.repos.d/repobak

\cp -f  ./localrepo.repo /etc/yum.repos.d/


echo "yum addr is"
echo $repoaddr
echo "master addr is $masterip"


os_release=`cat /etc/redhat-release |awk '{print$4}'`
if [ "$os_release" == "7.2.1511"  ]
then
sed -i "s|remote = .* relative|remote = \"$repoaddr/\" + relative|g" /usr/lib/python2.7/site-packages/yum/yumRepo.py
else
sed -i "s|remote = .* relative|remote = \"$repoaddr/\" + relative|g" /usr/lib/python2.6/site-packages/yum/yumRepo.py
fi

yum clean all
yum makecache

yum install salt-minion -y

sed -i "s|#master: salt|master: $masterip|g" /etc/salt/minion

service salt-minion start

if [ "$os_release" == "7.2.1511"  ]
then
sed -i "s|remote = .* relative|remote = \"$repoaddr/\" + relative|g" /usr/lib/python2.7/site-packages/yum/yumRepo.py
else
sed -i "s|remote = .* relative|remote = \"$repoaddr/\" + relative|g" /usr/lib/python2.6/site-packages/yum/yumRepo.py
fi
