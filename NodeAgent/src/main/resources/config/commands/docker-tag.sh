#!/bin/bash
registryAddr=$1
image=$2

docker tag $registryAddr"/"$image $image

docker rmi $registryAddr"/"$image
