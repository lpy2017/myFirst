#!/bin/bash
ISDHCPP=$1
MNAME=$2
DIRPATH=$3
WORKPATH=$4
IMAGE=$5
MEM=$6
MCPU=$7
IP=$8
MPOTS=$9




vlimit=''


if [ -n "$MEM" ]
	then
		vlimit=$vlimit' -m '$MEM
fi

if [ -n "$MCPU" ]
	then
		vlimit=$vlimit' --cpuset-cpus='$MCPU
fi


if [ '1' = "$ISDHCP" ]
then 
                OLD_IFS=$IFS
		IFS=','
		vports=''
		for tmp in $MPOTS
			do
				vports=$vports' -p '$tmp':'$tmp
			done
		IFS=$OLD_IFS
 
 
                docker create -it $vlimit  -e HOST_IP=${IP} $vports --name ${MNAME} $IMAGE
        else
 
                docker create -it  $vlimit -e HOST_IP=${IP} --net=none --name ${MNAME} $IMAGE

		 
 
 
fi
 
