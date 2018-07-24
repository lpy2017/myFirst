#!/bin/bash

brigeName=bridge0

netBridgeIp=`ifconfig $brigeName|grep "inet "|awk -F : {'print$2'}|awk -F " " {'print$1'}`

mask=`ifconfig $brigeName|sed -n '/Mask:/ s/.*Mask://pg'`

if [ -z $mask ]
then
mask=`ifconfig $brigeName|sed -n '/язбК:/ s/.*язбК://pg'`
fi

echo $netBridgeIp:$mask
