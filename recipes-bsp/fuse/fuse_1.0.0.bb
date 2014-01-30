DESCRIPTION = "Mount host filesystem"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"

PR = "r0"
#SRC_URI = "file://fuse.sh"
SRC_URI  = "file://fuse-1.0.0.tar.gz"

do_install(){
        install -d ${D}${sysconfdir}/init.d
        install -d ${D}${sysconfdir}/rc5.d
	install -m 0755 ${WORKDIR}/fuse.sh ${D}${sysconfdir}/init.d
 	ln -sf  ../init.d/fuse.sh    ${D}${sysconfdir}/rc5.d/S99fuse
        install -d ${D}${bindir}
        install -m 0111 ${WORKDIR}/fuse_host_access.exe ${D}${bindir}
}
 

