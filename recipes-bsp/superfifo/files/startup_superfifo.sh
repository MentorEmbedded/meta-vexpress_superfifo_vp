#!/bin/sh

echo "add driver"
modprobe -i super_fifo_drv
mknod /dev/superFIFODev c 89 0




