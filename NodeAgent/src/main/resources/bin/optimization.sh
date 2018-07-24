#!/bin/bash
echo "***************** 修改信息开始 *********************************************************************************"

echo '一。在/etc/sysctl.conf配置文件下最下面添加%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%'
echo 'net.ipv4.tcp_keepalive_intvl = 15'
echo 'net.ipv4.tcp_keepalive_probes = 5'
echo 'net.ipv4.tcp_keepalive_time = 300'

echo 'net.ipv4.ip_local_port_range=1024   65000'
echo 'net.ipv4.tcp_tw_reuse = 1' 
echo 'net.ipv4.tcp_max_tw_buckets = 30000'

echo 'net.nf_conntrack_max = 655360'
echo 'net.netfilter.nf_conntrack_tcp_timeout_established = 60'
echo 'net.netfilter.nf_conntrack_tcp_timeout_time_wait = 30'



echo '1）设置net.ipv4.tcp_keepalive_intvl = 15'
count=$(grep -c -o ^net.ipv4.tcp_keepalive_intvl /etc/sysctl.conf)
if [ $count -eq 0 ]
then
echo net.ipv4.tcp_keepalive_intvl = 15 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.tcp_keepalive_intvl.*#net.ipv4.tcp_keepalive_intvl = 15#g' /etc/sysctl.conf
fi

echo '2）设置net.ipv4.tcp_keepalive_probes = 5'
count=$(grep -c -o ^net.ipv4.tcp_keepalive_probes /etc/sysctl.conf)
if [ $count -eq 0 ]
then
echo net.ipv4.tcp_keepalive_probes = 5 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.tcp_keepalive_probes.*#net.ipv4.tcp_keepalive_probes = 5#g' /etc/sysctl.conf
fi

echo '3）设置net.ipv4.tcp_keepalive_time = 300'
count=$(grep -c -o ^net.ipv4.tcp_keepalive_time /etc/sysctl.conf)
if [ $count -eq 0 ]
then
echo net.ipv4.tcp_keepalive_time = 300 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.tcp_keepalive_time.*#net.ipv4.tcp_keepalive_time = 300#g' /etc/sysctl.conf
fi


echo '4）设置net.ipv4.ip_local_port_range=1024   65000'
count=$(grep -c -o ^net.ipv4.ip_local_port_range /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.ipv4.ip_local_port_range=1024   65000 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.ip_local_port_range.*#net.ipv4.ip_local_port_range =1024   65000#g' /etc/sysctl.conf
fi

echo '5）设置net.ipv4.tcp_tw_reuse = 1'
count=$(grep -c -o ^net.ipv4.tcp_tw_reuse /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.ipv4.tcp_tw_reuse = 1 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.tcp_tw_reuse.*#net.ipv4.tcp_tw_reuse = 1#g' /etc/sysctl.conf
fi


echo '6）设置net.ipv4.tcp_max_tw_buckets = 30000'
count=$(grep -c -o ^net.ipv4.tcp_max_tw_buckets /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.ipv4.tcp_max_tw_buckets = 30000 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.tcp_max_tw_buckets.*#net.ipv4.tcp_max_tw_buckets = 30000#g' /etc/sysctl.conf
fi

echo '7）设置net.nf_conntrack_max = 655360'
count=$(grep -c -o ^net.nf_conntrack_max /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.nf_conntrack_max = 655360 >>/etc/sysctl.conf
else
sed -i 's#^net.nf_conntrack_max.*#net.nf_conntrack_max = 655360#g' /etc/sysctl.conf
fi

echo '8）设置net.netfilter.nf_conntrack_tcp_timeout_established = 60'
count=$(grep -c -o ^net.netfilter.nf_conntrack_tcp_timeout_established /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.netfilter.nf_conntrack_tcp_timeout_established = 60 >>/etc/sysctl.conf
else
sed -i 's#^net.netfilter.nf_conntrack_tcp_timeout_established.*#net.netfilter.nf_conntrack_tcp_timeout_established = 60#g' /etc/sysctl.conf
fi

echo '9）设置net.netfilter.nf_conntrack_tcp_timeout_time_wait = 30'
count=$(grep -c -o ^net.netfilter.nf_conntrack_tcp_timeout_time_wait /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.netfilter.nf_conntrack_tcp_timeout_time_wait = 60 >>/etc/sysctl.conf
else
sed -i 's#^net.netfilter.nf_conntrack_tcp_timeout_time_wait.*#net.netfilter.nf_conntrack_tcp_timeout_time_wait = 60#g' /etc/sysctl.conf
fi

echo '9）设置net.ipv4.tcp_tw_recycle = 1'
count=$(grep -c -o ^net.ipv4.tcp_tw_recycle /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.ipv4.tcp_tw_recycle = 1 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.tcp_tw_recycle.*#net.ipv4.tcp_tw_recycle = 1#g' /etc/sysctl.conf
fi

echo '9）设置net.ipv4.ip_forward = 1'
count=$(grep -c -o ^net.ipv4.ip_forward /etc/sysctl.conf)
if [ $count  -eq 0 ]
then
echo net.ipv4.ip_forward = 1 >>/etc/sysctl.conf
else
sed -i 's#^net.ipv4.ip_forward.*#net.ipv4.ip_forward = 1#g' /etc/sysctl.conf
fi


echo '10）执行sysctl -p，设置生效'
sysctl -p

echo '11）执行sysctl -a查看这些参数是否被修改成功'

sysctl -a|grep -E 'net.ipv4.tcp_keepalive_intvl|net.ipv4.tcp_keepalive_probes|net.ipv4.tcp_keepalive_time'

sysctl -a|grep -E 'net.ipv4.ip_local_port_range|net.ipv4.tcp_tw_reuse|net.ipv4.tcp_max_tw_buckets'



echo -e  '\n' 


echo '二。设置最大打开文件数，修改/etc/security/limits.conf，添加如下行：%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%'
echo '* hard nofile 65535'
echo '* soft nofile 65535'


echo '1）添加 * hard nofile 65535 到/etc/security/limits.conf'
count=$(grep -c -o '^* hard nofile 65535' /etc/security/limits.conf)
if [ $count -eq 0 ]
then
echo '* hard nofile 65535'>>/etc/security/limits.conf
fi

echo '2）添加 * soft nofile 65535到/etc/security/limits.conf'
count=$(grep -c -o '^* soft nofile 65535' /etc/security/limits.conf)
if [ $count -eq 0 ]
then
echo '* soft nofile 65535'>>/etc/security/limits.conf
fi

echo '3）判断是否为ubuntu系统，如果是则要修改如下内容'
echo 'A. 打开/etc/pam.d/su文件，将session required pam_limits.so这行注释放开；'
echo 'B. 在/etc/profile文件末尾添加一行：ulimit -SHn 65535'

lsb_release -a
count=$(lsb_release -a|grep -E 'Ubuntu|ubuntu'|wc -l)
echo "count: $count"
if [ $count -eq 0 ]
then
echo '此系统不是ubuntu系统，不需要修改'
else
echo '此系统是ubuntu系统，需要修改'
echo 'A. 打开/etc/pam.d/su文件，将session required pam_limits.so这行注释放开；'

sed -i 's/^#*\(session.*\)/\1 /g' /etc/pam.d/su


echo 'B. 在/etc/profile文件末尾添加一行：ulimit -SHn 65535'

count=$(grep -c -o '^ulimit -SHn 65535' /etc/profile)
if [ $count -eq 0 ]
then
echo 'ulimit -SHn 65535'>>/etc/profile
fi

fi

echo '3）执行ulimit -a 验证是否配置生效'
ulimit -a  




echo -e  '\n' 


echo '三。设置最大进程数，修改配置文件/etc/security/limits.conf或/etc/security/limits.d/20-nproc.conf，添加如下行：%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%'
echo '* soft nproc 65535'
echo '* hard nproc 65535'




#ubuntu操作系统
if lsb_release -i |grep -i 'ubuntu'
then
version=$(lsb_release -r -s)
echo "Currnet OS is Ubuntu"$version

echo '1）添加 * soft nproc 65535 到/etc/security/limits.conf'
count=$(grep -c -o '^* soft nproc 65535' /etc/security/limits.conf)
if [ $count -eq 0 ]
then
echo '* soft nproc 65535'>>/etc/security/limits.conf
fi

echo '2）添加 * hard nproc 65535到/etc/security/limits.conf'
count=$(grep -c -o '^* hard nproc 65535' /etc/security/limits.conf)
if [ $count -eq 0 ]
then
echo '* hard nproc 65535'>>/etc/security/limits.conf
fi

fi


#centos操作系统
if cat /etc/redhat-release |grep -i 'centos'
then
version=$(cat /etc/redhat-release | awk '{print $4}')
echo "Currnet OS is CentOS "$version
result=$(awk -v num1=$version -v num2=6.5 'BEGIN{print(num1>=num2)?"0":"1"}')
if [ $result -eq 0 ]
then
echo "os version >=6.5"
echo '1）添加 * soft nproc 65535 到/etc/security/limits.d/20-nproc.conf'
count=$(grep -c -o '^* soft nproc 65535' /etc/security/limits.d/20-nproc.conf)
if [ $count -eq 0 ]
then
echo '* soft nproc 65535'>>/etc/security/limits.d/20-nproc.conf
fi

echo '2）添加 * hard nproc 65535到/etc/security/limits.d/20-nproc.conf'
count=$(grep -c -o '^* hard nproc 65535' /etc/security/limits.d/20-nproc.conf)
if [ $count -eq 0 ]
then
echo '* hard nproc 65535'>>/etc/security/limits.d/20-nproc.conf
fi

else
echo "os version <6.5"
echo '1）添加 * soft nproc 65535 到/etc/security/limits.conf'
count=$(grep -c -o '^* soft nproc 65535' /etc/security/limits.conf)
if [ $count -eq 0 ]
then
echo '* soft nproc 65535'>>/etc/security/limits.conf
fi

echo '2）添加 * hard nproc 65535到/etc/security/limits.conf'
count=$(grep -c -o '^* hard nproc 65535' /etc/security/limits.conf)
if [ $count -eq 0 ]
then
echo '* hard nproc 65535'>>/etc/security/limits.conf
fi

fi

fi


echo "***************** 修改信息结束 *********************************************************************************"



