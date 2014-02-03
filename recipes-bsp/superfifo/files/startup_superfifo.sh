#!/bin/sh

echo "add driver"
modprobe -i sfifo_drv
mknod /dev/superFIFODev c 89 0




