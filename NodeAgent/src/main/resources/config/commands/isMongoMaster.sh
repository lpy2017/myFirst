#!/bin/bash
shelldir=$1
bash $shelldir/isPrimary.sh |grep isMaster
