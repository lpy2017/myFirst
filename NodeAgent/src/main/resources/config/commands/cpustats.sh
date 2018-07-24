#!/bin/bash
re=`top -b -n1 |grep -i cpu |grep -iv mem`
echo $re
