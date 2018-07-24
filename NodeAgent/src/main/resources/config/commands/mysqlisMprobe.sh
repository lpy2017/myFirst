#!/bin/bash
cat /etc/mysql/my.cnf|grep -w server-id|grep -v '#'|cut -d "=" -f2
