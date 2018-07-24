#!/bin/bash
pid=$1
ps aux | awk '{print($2" "$3" "$4);}' |grep $pid
