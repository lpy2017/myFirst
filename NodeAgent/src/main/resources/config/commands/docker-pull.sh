#!/bin/bash
registryAddr=$1
image=$2

docker pull $registryAddr"/"$image
