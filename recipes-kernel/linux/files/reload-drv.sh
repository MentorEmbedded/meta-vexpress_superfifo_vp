modprobe -r super_fifo_drv
cp /lib/modules/3.6.8/kernel/driver/sfifo_drv/super_fifo_drv.ko /
insmod  /super_fifo_drv.ko
lsmod
