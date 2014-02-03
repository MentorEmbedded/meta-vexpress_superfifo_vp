modprobe -r sfifo_drv
cp /lib/modules/3.6.8/kernel/driver/sfifo_drv/sfifo_drv.ko /
insmod  /sfifo_drv.ko
lsmod
