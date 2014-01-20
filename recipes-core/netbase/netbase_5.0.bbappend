# Add a custom interfaces file with a static configuration

PRINC := "${@int(PRINC) + 1}"

SRC_URI += "file://interfaces"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"


