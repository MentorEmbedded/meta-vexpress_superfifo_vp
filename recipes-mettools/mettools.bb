SUMMARY = "Mentor Embedded Tracing Tools for LTTng 2.0"
DESCRIPTION = "Provides basic infrastructure for LTTng 2.0 tracing with Sourcery Analyzer."
HOMEPAGE = "http://go.mentor.com/sourceryanalyzer"
BUGTRACKER = "n/a"

LICENSE = "LGPLv2.1+ & BSD"
LIC_FILES_CHKSUM = "file://mettools/tracepoint.h;md5=61133c446e622e1f8a34cad7f6a2114e"

inherit autotools

DEPENDS = "lttng2-ust"

PV = "1.0"
PR = "r2"

FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"
SRC_URI = " \
    file://mettools-1.2.0.tar.bz2 \
    "

COMPATIBLE_HOST = '(x86_64.*|i.86.*|arm.*|powerpc.*)-linux'
