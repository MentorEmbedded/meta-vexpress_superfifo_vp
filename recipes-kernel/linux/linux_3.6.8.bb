DESCRIPTION = "Mainline Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel
#MEL6 linux-dtb is broken for recent DTS sources in recent kernels:
#require recipes-kernel/linux/linux-dtb.inc
#append DTB recipes directly instead...

SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v3.x/linux-${PV}.tar.bz2;name=kernel \
           git://linux-arm.org/boot-wrapper.git;name=bootwrapper;destsuffix=${PN}-${PV}/arch/${ARCH}/boot/boot-wrapper \
           file://defconfig \
           file://0001-sfifo_demo_ws_19_Aug.patch \
           file://0001-SuperFIFO-Virtual-Prototype-Demo-Driver.patch \
           file://0001-SuperFIFO-Virtual-Prototype-DTS-updates.patch \
           file://0001-SuperFIFO-Virtual-Prototype-Demo-Driver-Sep-03-2013-.patch \
           file://0001-ARM-vexpress-DTS-update-to-include-changes-in-recent.patch \
           file://0001-ARM-vexpress-defconfig-update-to-include-changes-in-.patch \
           file://0001-ARM-vexpress-dts-set-bootargs-via-CMDLINE-and-disabl.patch \
           file://0001-Mentor-Graphics-vexpress_superfifo_vp-boot-wrapper-i.patch \
          "

# v3.6.8:
SRC_URI[kernel.md5sum] = "737f103da92ae07e81277e668a499850"
SRC_URI[kernel.sha256sum] = "4083fe48afd86e2271d7a866b6f26c127810f38d552df2b633612f323ef3b385"

# v3.8:
#SRC_URI[kernel.md5sum] = "fcd1d2e60e1033c935a13ef81c89ea2d"
#SRC_URI[kernel.sha256sum] = "fce774b5313e73949cb35f128e91e7b2ccd7fa2438abc5cff69267e504395a45"

SRCREV_bootwrapper = "${AUTOREV}"
BW="${S}/arch/${ARCH}/boot/boot-wrapper"

COMPATIBLE_MACHINE = "vexpress_superfifo_vp"

#INSANE_SKIP = "${PN}"
# The Virtual Prototype uses ELF images which aren't stripped, so inhibit package debug splitting:
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

# We need our own kernel_do_compile when building kernels >3.7 until
# following commit is applied to meta-mentor:
# 4cb20fa8 kernel.bbclass: remove explicit version.h target
#kernel_do_compile() {
#	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
#
#	oe_runmake ${KERNEL_IMAGETYPE_FOR_MAKE} ${KERNEL_ALT_IMAGETYPE} CC="${KERNEL_CC}" LD="${KERNEL_LD}"
#	if test "${KERNEL_IMAGETYPE_FOR_MAKE}.gz" = "${KERNEL_IMAGETYPE}"; then
#		gzip -9c < "${KERNEL_IMAGETYPE_FOR_MAKE}" > "${KERNEL_OUTPUT}"
#	fi
#}

#         KERNEL="${S}/arch/${ARCH}/boot/zImage" \
#         DTB="${S}/arch/${ARCH}/boot/vexpress-v2p-ca9.dtb" \

do_compile_append() {
    cd ${BW}
    oe_runmake -e \
         CC="${CC}" LD="${LD}" \
         FILESYSTEM="" \
         IMAGE="linux-system.axf" \
         BOOTARGS='"${BOOTARGS_COMMON} root=/dev/mmcblk0"'
}

do_deploy_append() {
        install -d ${DEPLOYDIR}
#        install -m 0644 ${S}/arch/${ARCH}/boot/vexpress-v2p-ca9.dtb ${DEPLOYDIR}/${KERNEL_IMAGE_BASE_NAME}.dtb
        install -m 0644 ${BW}/linux-system.axf ${DEPLOYDIR}/linux-system-${KERNEL_IMAGE_BASE_NAME}.axf
        cd ${DEPLOYDIR}
        ln -sf linux-system-${KERNEL_IMAGE_BASE_NAME}.axf linux-system.axf

	if test -n "${KERNEL_DEVICETREE}"; then
		for DTB in ${KERNEL_DEVICETREE}; do
			if echo ${DTB} | grep -q '/dts/'; then
				bbwarn "${DTB} contains the full path to the the dts file, but only the dtb name should be used."
				DTB=`basename ${DTB} | sed 's,\.dts$,.dtb,g'`
			fi
			DTB_BASE_NAME=`basename ${DTB} .dtb`
			DTB_NAME=`echo ${KERNEL_IMAGE_BASE_NAME} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
			DTB_SYMLINK_NAME=`echo ${KERNEL_IMAGE_SYMLINK_NAME} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
			DTB_PATH="${B}/arch/${ARCH}/boot/dts/${DTB}"
			if [ ! -e "${DTB_PATH}" ]; then
				DTB_PATH="${B}/arch/${ARCH}/boot/${DTB}"
			fi
			install -d ${DEPLOYDIR}
			install -m 0644 ${DTB_PATH} ${DEPLOYDIR}/${DTB_NAME}.dtb
			cd ${DEPLOYDIR}
			ln -sf ${DTB_NAME}.dtb ${DTB_SYMLINK_NAME}.dtb
			cd -
		done
	fi
}


do_install_append() {
	if test -n "${KERNEL_DEVICETREE}"; then
		for DTB in ${KERNEL_DEVICETREE}; do
			if echo ${DTB} | grep -q '/dts/'; then
				bbwarn "${DTB} contains the full path to the the dts file, but only the dtb name should be used."
				DTB=`basename ${DTB} | sed 's,\.dts$,.dtb,g'`
			fi
			DTB_BASE_NAME=`basename ${DTB} .dtb`
			DTB_NAME=`echo ${KERNEL_IMAGE_BASE_NAME} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
			DTB_SYMLINK_NAME=`echo ${KERNEL_IMAGE_SYMLINK_NAME} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
			DTB_PATH="${B}/arch/${ARCH}/boot/dts/${DTB}"
			oe_runmake ${DTB}
			if [ ! -e "${DTB_PATH}" ]; then
				DTB_PATH="${B}/arch/${ARCH}/boot/${DTB}"
			fi
			install -m 0644 ${DTB_PATH} ${D}/${KERNEL_IMAGEDEST}/devicetree-${DTB_SYMLINK_NAME}.dtb
		done
	fi
}

pkg_postinst_kernel-devicetree () {
	cd /${KERNEL_IMAGEDEST}
	for DTB_FILE in ${KERNEL_DEVICETREE}
	do
		DTB_BASE_NAME=`basename ${DTB_FILE} | awk -F "." '{print $1}'`
		DTB_SYMLINK_NAME=`echo ${KERNEL_IMAGE_SYMLINK_NAME} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
		update-alternatives --install /${KERNEL_IMAGEDEST}/${DTB_BASE_NAME}.dtb ${DTB_BASE_NAME}.dtb devicetree-${DTB_SYMLINK_NAME}.dtb ${KERNEL_PRIORITY} || true
	done
}

pkg_postrm_kernel-devicetree () {
	cd /${KERNEL_IMAGEDEST}
	for DTB_FILE in ${KERNEL_DEVICETREE}
	do
		DTB_BASE_NAME=`basename ${DTB_FILE} | awk -F "." '{print $1}'`
		DTB_SYMLINK_NAME=`echo ${KERNEL_IMAGE_SYMLINK_NAME} | sed "s/${MACHINE}/${DTB_BASE_NAME}/g"`
		update-alternatives --remove ${DTB_BASE_NAME}.dtb devicetree-${DTB_SYMLINK_NAME}.dtb ${KERNEL_PRIORITY} || true
	done
}
