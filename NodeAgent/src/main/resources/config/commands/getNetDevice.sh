#!/bin/bash
#获取连接外网的网卡
catnum=`route -n|grep UG |grep -E -v '(UGH)'|wc -l`
if [[ $catnum -eq 1   ]]
then
        route -n|grep UG |grep -E -v '(UGH)' |awk  '{print $8}'
        exit 1
elif [[ $catnum -eq 0 ]]
        then
        echo "no device"
        #在这里处理

        exit 1;
fi
list=`route -n|grep UG |grep -E -v '(UGH)' |awk  '{print $8}'`
#如果结果大于1才会进行筛选
findNum=0
for eth in $list
do
        interrupt=`ifconfig $eth |grep "nterrupt" |wc -l`
	device=`ifconfig $eth |grep "device" |wc -l`
        if (( interrupt == 1 )) || (( device == 1 ))
        then
		((findNum++))
                echo $eth
        fi
done
if (( findNum == 0 ))
then
	for eth in $list
	do
        if [[ $eth != 'ovs0'  ]]
        then
                echo $eth
        fi
done
fi
