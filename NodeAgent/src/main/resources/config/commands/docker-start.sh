#!/bin/bash
CNAME=$1
ISRESTART=$2

if [ -n "$ISRESTART" ]
then
	docker restart $CNAME
else 
     	docker start $CNAME
fi



