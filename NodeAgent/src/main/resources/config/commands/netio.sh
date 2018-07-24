#!/bin/bash
ethName=$1
grep  $ethName /proc/net/dev
