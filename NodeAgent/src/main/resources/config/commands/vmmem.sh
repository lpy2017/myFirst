#!/bin/bash
port=$1
sudo jstat -gc $port
