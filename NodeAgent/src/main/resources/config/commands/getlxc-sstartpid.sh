#!/bin/bash
upath=$1
RESULT=$(ps -fe|grep $upath|grep -E "java|start")
echo ${RESULT}|awk {'print$2'}