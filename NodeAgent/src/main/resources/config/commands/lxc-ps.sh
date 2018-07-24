#!/bin/bash
upath=$1
ps -fe|grep $upath|grep -E "java|start"