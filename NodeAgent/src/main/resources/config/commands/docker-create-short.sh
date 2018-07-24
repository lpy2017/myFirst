#!/bin/bash
#create a container for short task  ,and it need to communicate to other paas container
MNAME=$1
IMAGE=$2
MEM=$3
CPUSHARE=$4
CPUSET=$5
NETMODEL=$6
VOLUME=$7


#mem share
if [ -n "$MEM" ]
	then
		vlimit=$vlimit' -m '$MEM
fi

#cpu share
if [  "$CPUSHARE" != "-1" ] 
	then
	vlimit=$vlimit' --cpu-shares='$CPUSHARE
	if [ -n "$CPUSET" ]
		then
			vlimit=$vlimit' --cpuset-cpus='$CPUSET
	fi
	
fi

if [ -n "$VOLUME" ]
then

OLD_IFS=$IFS
IFS=','
strarg=($VOLUME)
for part in ${strarg[@]}
do
volume=${volume}' -v  '${part}

done
IFS=$OLD_IFS
fi

#echo 'sudo docker create -it   '$vlimit'   '$volume' --net=none  --privileged=true --name '${MNAME}' '$IMAGE' 2>&1'
a=`sudo docker create -it   $vlimit   $volume --net=$NETMODEL  --privileged=true --name ${MNAME} $IMAGE 2>&1`
echo "success"
cre=`sudo docker inspect --format="{{.State.Status}}" $MNAME`
if [[ $cre == 'created' ]]
then
echo "success"
fi

