#!/bin/bash

docker inspect --format='{{.State.Pid}}{{.Name}}' `docker ps -a -q`
