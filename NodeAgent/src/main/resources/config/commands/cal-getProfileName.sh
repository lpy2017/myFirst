#!bash/bin
#profile name
source /etc/profile
sudo -E calicoctl profile show |sed -n '4p' |awk '{print $2}'