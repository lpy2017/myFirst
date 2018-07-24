#!/bin/bash
ISDHCP=$1
MNAME=$2
##########HPORT=$3
##########DPORT=$4
IMAGE=$3
NETMODEL=$4
MEM=$5
MCPU=$6
CPUSHARE=$7
CIP=$8
CID=${9}
ENV=${10}
VOLUME=${11}

vlimit=''
env=''
volume=''
#vports=''

if [ -n "$ENV" ]
then
 
OLD_IFS=$IFS
IFS=','
strarg=($ENV)
for part in ${strarg[@]}
do
key=$(echo $part|cut -d : -f 1)
value=$(echo ${part#*:})

env=${env}' -e '${key}'='${value}

done
IFS=$OLD_IFS
fi


if [ -n "$VOLUME" ]
then

OLD_IFS=$IFS
IFS=','
strarg=($VOLUME)
for part in ${strarg[@]}
do
#key=$(echo $part|cut -d : -f 1)
#value=$(echo ${part#*:})

volume=${volume}' -v  '${part}

done
IFS=$OLD_IFS
fi

 

if [ -n "$MEM" ]
	then
		vlimit=$vlimit' -m '$MEM
fi

if [ -n "$MCPU" ]
	then
		vlimit=$vlimit' --cpuset-cpus='$MCPU
fi

if [  "$CPUSHARE" != "-1" ] 
	then
	if [ "$CPUSHARE" -lt 2 ] ; then
		 CPUSHARE=2
	fi
	
	vlimit=$vlimit' --cpu-shares='$CPUSHARE
fi
ADD_HOST=""
if [ "host" !=  $NETMODEL ]
then
	ADD_HOST="--add-host=${CID}:${CIP}"
fi
if [ '1' = "$ISDHCP" ]
	then 
		if [ "$HPORT" != "-1" -o "$DPORT" != "-1" ] ; then
		    vports='-p '$HPORT':'$DPORT
		fi
		
		docker create -it $vlimit $env $volume  $vports --hostname=${CID}  --add-host=${CID}:${CIP} --privileged=true --name ${MNAME} $IMAGE 2>&1
	else
		
		docker create -it  $ADD_HOST  $vlimit $env  $volume --net=$NETMODEL --privileged=true --name ${MNAME} $IMAGE 2>&1

#echo docker create -it $vlimit $env  $volume --net=none --hostname=${CID}  --add-host=${CID}:${CIP} --name ${MNAME} $IMAGE		 
 
fi




