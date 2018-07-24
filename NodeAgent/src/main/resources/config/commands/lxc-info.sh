
#!/bin/bash
APP=$1
if [ "x${APP}" != "x" ]
then
    lxc-info -n ${APP}
 else
    echo FASLE
 fi
   
