MNAME=$1
NETADDR=$2
shortname=${MNAME#*'CN'}
pid=$(docker inspect -f '{{.State.Pid}}' $MNAME)


#if [ -d "/var/run/netns" ]
#then
#  mkdir -p  /var/run/netns
#fi
#find -L /var/run/netns -type l -delete
#find -L /var/run/netns -type l|grep -w ${pid}|xargs rm -f

sudo mkdir -p /var/run/netns
sudo find -L /var/run/netns -type l -delete
 
if [ -f /var/run/netns/$pid ]; then
   sudo rm -f /var/run/netns/$pid
fi


sudo ln -s /proc/$pid/ns/net /var/run/netns/$pid

sudo ip link add vetha${shortname} type veth peer name vethb${shortname} 
      
sudo brctl addif bridge0 vethb${shortname} 
      

sudo ip link set vethb${shortname} up 

sudo ip link set vetha${shortname} netns $pid 

sudo ip netns exec $pid ip link set dev vetha${shortname} name eth0 

sudo ip netns exec $pid ip link set eth0 up 


sudo ip netns exec $pid ip addr add ${NETADDR}.${shortname}/24 dev eth0


sudo ip netns exec $pid ip route add default via ${NETADDR}.1

pingcount=`docker exec $MNAME ping -c 4 ${NETADDR}.${shortname}  |grep -o "time="|wc -l`

if [ "0" = "$pingcount" ]
	then
	echo "net not ok"
	else
	echo ok
fi	


