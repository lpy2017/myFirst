#!/bin/bash  

file=${install_path}installredis.sh
args="\"redis ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${redislhost} file.mkdir ${install_path}"
salt-ssh ${redishost} file.mkdir ${install_path}

echo "salt-ssh '${redishost}' cp.get_file salt://paas/install.sh ${install_path}"
salt-ssh ${redishost} cp.get_file salt://paas/installredis.sh ${install_path} > /tmp/salt.log 2>&1

echo "salt-ssh ${redishost} cmd.script ${file} ${args}"
salt-ssh ${redishost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1