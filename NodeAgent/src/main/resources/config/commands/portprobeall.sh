#!/bin/bash
ports=$1
#echo $ports
netstat -anpt|grep ESTABLISHED|grep -wE ${ports//:/|}|awk '{print$4}'|awk -F: '{print$2}'|sort -n|uniq -c
