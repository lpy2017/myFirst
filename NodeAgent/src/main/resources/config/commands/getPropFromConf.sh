#!/bin/bash
pat=$1
shdir=$0

cd $(dirname $0) ;cd ..
echo `grep -n "${pat}" config.properties |cut -d "=" -f 2`