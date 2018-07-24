#!/bin/bash
CNAME=$1
ISFORCE=$2

if [ -n "$ISFORCE" ]
then
	docker kill $CNAME
else 
	docker stop -t 2 $CNAME
fi