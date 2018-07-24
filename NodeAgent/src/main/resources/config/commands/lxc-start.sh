#!/bin/bash
APP=$1
MEMORY=$2
CPU=$3

if  [ "x${APP}" != "x" ]
then
    lxc-stop -n ${APP}
    if [ "x${CPU}" == "x" ] && [ "x${MEMORY}" == "x" ]
    then
        lxc-start -n ${APP} -d
	echo TRUE
    fi

    if [ "x${CPU}" != "x" ] && [ "x${MEMORY}" == "x" ]
     then 
        lxc-start -n ${APP} -d -s lxc.cgroup.cpuset.cpus=${CPU}
        echo TRUE
    fi

    if [ "x${CPU}" == "x" ] && [ "x${MEMORY}" != "x" ]
     then
        lxc-start -n ${APP} -d -s lxc.cgroup.memory.limit_in_bytes=${MEMORY}
        echo TRUE
    fi

    if [ "x${CPU}" != "x" ] && [ "x${MEMORY}" != "x" ]
     then
        lxc-start -n ${APP} -d -s lxc.cgroup.cpuset.cpus=${CPU} -s lxc.cgroup.memory.limit_in_bytes=${MEMORY}
        echo TRUE
    fi

   
    
  else
     echo FASLE
  fi