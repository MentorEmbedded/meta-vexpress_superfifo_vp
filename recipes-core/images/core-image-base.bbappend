PRINC := "${@int(PRINC) + 1}"

IMAGE_INSTALL_append = " libstdc++"
IMAGE_INSTALL_append = " kernel-module-sfifo-drv"
IMAGE_INSTALL_append = " superfifo"
IMAGE_INSTALL_append = " mettools"
IMAGE_INSTALL_append = " fuse"

