#!/bin/bash
for i in $*
do
if [ -z $pids ]
then
pids=$i
else
 pids=$pids"|"$i
fi

done

top -bn1 | awk '/'$pids'/ {print $1,$9","$10}'
