cname=$1
cip=$2
pid=$(docker inspect -f '{{.State.Pid}}' CN${cname})

mkdir -p  /var/run/netns

ln -s /proc/$pid/ns/net /var/run/netns/$pid

ip link add vetha${cname} type veth peer name vethb${cname} 
      
brctl addif bridge0 vethb${cname} 
      

ip link set vethb${cname} up 

ip link set vetha${cname} netns $pid 

ip netns exec $pid ip link set dev vetha${cname} name eth0 

ip netns exec $pid ip link set eth0 up 

ip netns exec $pid ip addr add $cip/24 dev eth0

#do not set mac
#ip netns exec $pid ip link set dev eth0 address 02:42:0a:00:04:${cname}

ip netns exec $pid ip route add default via 10.0.5.1
