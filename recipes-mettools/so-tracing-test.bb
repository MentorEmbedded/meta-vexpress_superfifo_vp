SUMMARY = "Mentor Embedded Tracing Tools for LTTng 2.0 - Shared object Tracing Sample"
DESCRIPTION = "Demonstrates userspace tracing with LTTng 2.0 across several shared objects."
HOMEPAGE = "http://go.mentor.com/sourceryanalyzer"
BUGTRACKER = "n/a"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README;md5=603200ce7c7b664dbb7b4cc28142e803"

inherit autotools

DEPENDS = "mettools"

PV = "1.0"
PR = "r0"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI = " \
    file://so-tracing-test-1.0.tar.bz2 \
"

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'
