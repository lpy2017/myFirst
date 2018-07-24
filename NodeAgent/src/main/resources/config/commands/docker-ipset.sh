MNAME=$1
DOCKERIP=$2
NETADDR=$3
#DIGIT=$4
shortname=${MNAME#*'CN'}
pid=$(docker inspect -f '{{.State.Pid}}' $MNAME)

#�������������ռ�
sudo mkdir -p /var/run/netns
sudo find -L /var/run/netns -type l -delete

#���鲢��ȷ�����������ռ�������ɾ�� 
if [ -f /var/run/netns/$pid ]; then
    sudo rm -f /var/run/netns/$pid
fi

if [ -f /var/run/netns/$pid ];then
	sleep 2s
	sudo rm -f /var/run/netns/$pid
fi

if [ -f /var/run/netns/$pid ];then
	echo "not ok,current pid is:" ${ip} "exist,do not delete"
	exit 0
fi

#ɾ��bridge0�г�ͻ����������
sudo ifconfig vethb${shortname} > /dev/null 2>&1
if [ $? -eq 0 ]; then
   sudo ip link del vethb${shortname}
fi

sudo ln -s /proc/$pid/ns/net /var/run/netns/$pid
#����һ�Խӿڣ�b�󶨵�������
sudo ip link add vetha${shortname} type veth peer name vethb${shortname} 
      
sudo brctl addif bridge0 vethb${shortname} 
      
sudo ip link set vethb${shortname} up 

#a�ŵ����������������ռ䣬����Ϊeth0,����������ip
sudo ip link set vetha${shortname} netns $pid 

sudo ip netns exec $pid ip link set dev vetha${shortname} name eth0 

sudo ip netns exec $pid ip link set eth0 up 


sudo ip netns exec $pid ip addr add $DOCKERIP/24 dev eth0


sudo ip netns exec $pid ip route add default via ${NETADDR}

pingcount=`docker exec $MNAME ping -c 4 $DOCKERIP |grep -o "time="|wc -l`

if [ "0" = "$pingcount" ]
	then
	echo "net not ok"
	else
	echo ok
fi