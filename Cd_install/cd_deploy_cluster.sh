#!/bin/bash  

file1=${install_path}install_cluster1.sh
file2=${install_path}install_cluster2.sh
args="\"All_In_One ${install_path}\""
echo "deploy cluster1"
echo "salt-ssh ${mgrhost} file.mkdir ${install_path}"
salt-ssh ${mgrhost} file.mkdir ${install_path}
echo "salt-ssh '${mgrhost}' cp.get_file salt://paas/install_cluster1.sh ${install_path}"
nohup salt-ssh ${mgrhost} cp.get_file salt://paas/install_cluster1.sh ${install_path} >/tmp/salt.log 2>&1 &
echo "salt-ssh ${mgrhost} cmd.script ${file1} ${args}"
nohup salt-ssh ${mgrhost} cmd.script ${file1} "${args}" >> /tmp/salt.log 2>&1 &


echo "deploy cluster2"
echo "salt-ssh ${mgr2host} file.mkdir ${install_path}"
salt-ssh ${mgr2host} file.mkdir ${install_path}
echo "salt-ssh '${mgr2host}' cp.get_file salt://paas/install_cluster2.sh ${install_path}"
nohup salt-ssh ${mgr2host} cp.get_file salt://paas/install_cluster1.sh ${install_path} >/tmp/salt.log 2>&1 &
echo "salt-ssh ${mgr2host} cmd.script ${file2} ${args}"
nohup salt-ssh ${mgr2host} cmd.script ${file2} "${args}" >> /tmp/salt.log 2>&1 &
