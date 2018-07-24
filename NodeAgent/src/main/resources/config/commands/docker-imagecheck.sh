#!/bin/bash
image=$1
images=$(docker images|awk '{print $1":"$2}')

result="NO"

OLD_IFS=$IFS
IFS=$'\n'
for d in $images
do
#echo $d
if [ $d = $image ]
then
result="YES"
fi
done
IFS=$OLD_IFS

echo $result
