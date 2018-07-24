#!bash/bin
#add or remove profile on node

profile=$1
CIDRS=$2
op=$3
etcdUrl=$4

etcd_authority=$(cat /etc/profile | grep "export ETCD_AUTHORITY=")
if [[ "${etcd_authority}" == "" ]] ; then 
    sudo sh -c 'echo "export ETCD_AUTHORITY="'${etcdUrl}' >> /etc/profile'
else
    sudo sed -i 's/export ETCD_AUTHORITY=.*/export ETCD_AUTHORITY='${etcdUrl}'/g' /etc/profile
fi
source /etc/profile

if [ $op == "add" ] ; then
	sudo -E calicoctl pool remove 192.168.0.0/16
	sudo -E calicoctl pool add ${CIDRS} --ipip
	sudo -E calicoctl profile add ${profile}
	
elif [ $op == "remove" ]; then
	sudo -E calicoctl pool remove ${CIDRS}
	sudo -E calicoctl profile remove ${profile}

else
	echo "Error: unknown operation $op ."
fi


