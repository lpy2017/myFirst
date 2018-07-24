#!bin/bash

for veth in $@
    do
        total=`ovs-vsctl list-veths ovs0 |grep $veth`
        if [ -z $total ]
            then
                echo "do not exist"
		exit 0
        else
                ovs-vsctl del-veth $veth
        fi

        if ip link show "$veth" >/dev/null 2>&1; then
	    # link exists, is it in use?
	    if ip link show "$veth" up | grep -q "UP"; then
		echo "Ip link $veth exists and is up"
	    fi
	    # delete the link so we can re-add it afterwards
	    ip link del "$veth"
	fi
done;
