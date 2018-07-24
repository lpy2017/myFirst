#!/bin/bash
#create a container for short task  ,and it need to communicate to other paas container
MNAME=$1		#*container name 
IMAGE=$2		#*imagename 
CMD=$3			#*the command you want exec
MEM=$4			#memery share   'm'
CPUSHARE=$5
CPUSET=$6

VOLUME=$7		

CMD=${CMD//,/ }    #将CMD用，分割
CMDRESULT=""
for element in $CMD
do
    CMDRESULT=$CMDRESULT" "$element
done
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

if [ -n "$RMAFTERRUN" ]
then
	RMAFTERRUN=" --rm "
fi
result=`sudo docker run -itd --name ${MNAME} $vlimit $volume --privileged=true  $IMAGE   $CMDRESULT 2>&1`
sudo docker logs ${MNAME}


