SUMMARY = "inotify_simple Python module for the Ubuntu python3 of the rootfs"
DESCRIPTION = "Single-file Python wrapper around inotify, needed by \
flightstack-uploader. Ubuntu does not package it, and a Yocto python3 \
package would conflict with the Ubuntu rootfs, so only the module is \
installed into Ubuntu's dist-packages."
HOMEPAGE = "https://github.com/chrisjbillington/inotify_simple"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=28a8c10291bba0bc0f8b9d5e87c0d826"

SRCBRANCH = "master"

# Tag 2.0.1
SRCREV = "78524408ffb7eafa48b0b7061bf5d61ccff07824"

SRC_URI = "git://github.com/chrisjbillington/inotify_simple.git;protocol=https;branch=${SRCBRANCH}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d ${D}${prefix}/lib/python3/dist-packages
	install -m 0644 ${S}/inotify_simple.py ${D}${prefix}/lib/python3/dist-packages/
}

FILES:${PN} = "${prefix}/lib/python3/dist-packages/inotify_simple.py"
