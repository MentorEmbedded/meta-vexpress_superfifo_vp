modprobe -r super_fifo_drv
cp $MNT_WORKSPACE_LOC/sfifo_drv/sfifo_drv.ko /root/
insmod /root/sfifo_drv.ko
lsmod
