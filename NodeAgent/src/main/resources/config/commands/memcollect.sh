#!/bin/bash
free -m |grep em |awk '{print $2,$3,$4,$6}' 2>&1
