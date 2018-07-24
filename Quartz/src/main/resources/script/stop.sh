pid=`ps -ef|grep Quartz.jar|grep -v grep|awk '{print $2}'`
if [ -n "$pid" ]; then
    kill -9 $pid
    echo killed $pid
else
    echo quartz not running
fi
