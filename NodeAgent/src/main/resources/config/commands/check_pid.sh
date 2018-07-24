#!/bin/bash
pid=$1
ps -ef |awk '{print $2}'|grep -wc $pid
