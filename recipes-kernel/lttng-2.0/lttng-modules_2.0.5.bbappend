PRINC := "${@int(PRINC) + 2}"
SRC_URI = "git://git.lttng.org/lttng-modules.git;protocol=git \
           file://lttng-modules-replace-KERNELDIR-with-KERNEL_SRC.patch"
