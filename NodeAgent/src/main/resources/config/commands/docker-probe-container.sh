#!/bin/bash
CNAME=$1
docker ps --no-trunc |grep -w  $CNAME |wc -l
