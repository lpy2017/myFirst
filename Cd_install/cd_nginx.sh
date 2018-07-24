#!/bin/bash  

file=${install_path}installnginx.sh
args="\"nginx ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${nginxhost} file.mkdir ${install_path}"
salt-ssh ${nginxhost} file.mkdir ${install_path}

echo "salt-ssh '${nginxhost}' cp.get_file salt://paas/installnginx.sh ${install_path}"
salt-ssh ${nginxhost} cp.get_file salt://paas/installnginx.sh ${install_path} > /tmp/salt.log  2>&1

echo "salt-ssh ${nginxhost} cmd.script ${file} ${args}"
salt-ssh ${nginxhost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1
