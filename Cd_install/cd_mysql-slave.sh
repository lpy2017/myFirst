#!/bin/bash  

file=${install_path}installmysql-slave.sh
args="\"mysql ${install_path}\""
echo ${args}
echo ${file}

echo "salt-ssh ${mysqlhost} file.mkdir ${install_path}"
salt-ssh ${mysqlhost} file.mkdir ${install_path}

echo "salt-ssh '${mysqlhost}' cp.get_file salt://paas/install.sh ${install_path}"
salt-ssh ${mysqlhost} cp.get_file salt://paas/installmysql-slave.sh ${install_path} > /tmp/salt.log  2>&1

echo "salt-ssh ${mysqlhost} cmd.script ${file} ${args}"
salt-ssh ${mysqlhost} cmd.script ${file} "${args}" >> /tmp/salt.log 2>&1
