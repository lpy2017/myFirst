#!/bin/bash

brigeName=bridge0

netBridgeIp=`ifconfig $brigeName|grep "inet "|awk -F " " {'print$2'}`

mask=`ifconfig $brigeName|grep "netmask "|awk -F " " {'print$4'}`

#if [ -z $mask ]
#then
#mask=`ifconfig $brigeName|sed -n '/����:/ s/.*����://pg'`
#fi

echo $netBridgeIp:$mask
