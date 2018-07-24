#!/bin/bash
ps -fe|grep -w 'DENV_VARIABLE' |grep -v 'grep'|awk {'print$2'} |xargs kill -9
