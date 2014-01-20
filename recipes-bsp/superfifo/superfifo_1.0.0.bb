DESCRIPTION = "Move sfifo device to /dev"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"

PR = "r0"
SRC_URI = "file://startup_superfifo.sh"

do_install(){
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/rc5.d
	install -m 0755 ${WORKDIR}/startup_superfifo.sh ${D}${sysconfdir}/init.d 
 	ln -sf  ../init.d/startup_superfifo.sh    ${D}${sysconfdir}/rc5.d/S99startup_superfifo
}
 

