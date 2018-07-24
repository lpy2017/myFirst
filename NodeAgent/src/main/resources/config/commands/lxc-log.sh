#!/bin/bash
CHUSER=$1
LOGDIR=$2

if [ -d "$LOGDIR" ]
	then
	usr=$(ls -lnd|awk '{print$3}')
        gup=$(ls -lnd|awk '{print$4}')
	usrngup=$usr":"$gup
	echo $usrngup
	if [ "$usrngup" = "$CHUSER" ]
        	then
			echo ok
		else
			chown -v $CHUSER  $LOGDIR >${LOGDIR}/tmp.log
	fi
else	
	mkdir -p $LOGDIR
	chown -v $CHUSER  $LOGDIR >${LOGDIR}/tmp.log
        echo ok
fi
