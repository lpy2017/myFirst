#!/bin/bash  

file=${install_path}installdb.sh
args="\"database ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${dbhost} file.mkdir ${install_path}"
salt-ssh ${dbhost} file.mkdir ${install_path}

echo "salt-ssh '${dbhost}' cp.get_file salt://paas/installdb.sh ${install_path}"
salt-ssh ${dbhost} cp.get_file salt://paas/installdb.sh ${install_path} > /tmp/salt.log  2>&1

echo "salt-ssh ${dbhost} cmd.script ${file} ${args}"
salt-ssh ${dbhost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1
