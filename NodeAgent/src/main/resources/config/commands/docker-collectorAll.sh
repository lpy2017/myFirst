#!/bin/bash
CNAMES=$*

docker stats --no-stream $CNAMES
