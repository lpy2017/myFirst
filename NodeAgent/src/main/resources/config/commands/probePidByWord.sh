#!/bin/bash
for i in $@
do
	if [[ -z $keyword ]]
	then
		keyword=$i
	else
		keyword=$keyword"\\s"$i
	fi
	
done
sudo ps -ef|grep -E $keyword |grep -v color |awk '{print $2}'
