#!/bin/bash
instanceId=$1

ps -fe|grep -w 'DENV_VARIABLE'|grep -w $instanceId |grep -v 'grep'|awk {'print$2'} |xargs kill -9
