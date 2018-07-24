#!/bin/bash  

file=${install_path}installconfigcenter.sh
args="\"configcenter ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${configcenterhost} file.mkdir ${install_path}"
salt-ssh ${configcenterhost} file.mkdir ${install_path}

echo "salt-ssh '${configcenterhost}' cp.get_file salt://paas/install.sh ${install_path}"
salt-ssh ${configcenterhost} cp.get_file salt://paas/installconfigcenter.sh ${install_path} > /tmp/salt.log  2>&1

echo "salt-ssh ${configcenterhost} cmd.script ${file} ${args}"
salt-ssh ${configcenterhost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1
