#!/bin/bash  

file=${install_path}installAllInOne.sh
args="\"All_In_One ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${mgrhost} file.mkdir ${install_path}"
salt-ssh ${mgrhost} file.mkdir ${install_path}

echo "salt-ssh '${mgrhost}' cp.get_file salt://paas/install.sh ${install_path}"
salt-ssh ${mgrhost} cp.get_file salt://paas/installAllInOne.sh ${install_path} > /tmp/salt.log 2>&1

echo "salt-ssh ${mgrhost} cmd.script ${file} ${args}"
salt-ssh ${mgrhost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1
