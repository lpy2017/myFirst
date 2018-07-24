#!/bin/bash  

file=${install_path}installmongo.sh
args="\"mongodb ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${mongodbhost} file.mkdir ${install_path}"
salt-ssh ${mongodbhost} file.mkdir ${install_path}

echo "salt-ssh '${mongodbhost}' cp.get_file salt://paas/install.sh ${install_path}"
salt-ssh ${mongodbhost} cp.get_file salt://paas/installmongo.sh ${install_path} > /tmp/salt.log  2>&1

echo "salt-ssh ${mongodbhost} cmd.script ${file} ${args}"
salt-ssh ${mongodbhost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1
