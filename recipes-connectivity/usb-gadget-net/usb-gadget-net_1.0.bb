SUMMARY = "USB network gadget on the NavQ95 USB1 Type-C port (Skynode-style 10.41.1.1)"
DESCRIPTION = "Creates a configfs CDC-NCM (or ECM) gadget on the USB1 port, names \
the interface usbc, assigns 10.41.1.1/24 and runs udhcpd to hand the host a \
single lease (10.41.1.2). NetworkManager is told to leave the interface alone. \
udhcpd itself comes from the Ubuntu 'udhcpd' package (APTGET_EXTRA_PACKAGES)."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://usb-gadget-net \
    file://usb-gadget-net.default \
    file://usb-gadget-net.service \
    file://usb-gadget-net-dhcp.service \
    file://udhcpd.conf \
    file://99-usb-gadget-net-unmanaged.conf \
"

inherit systemd
SYSTEMD_SERVICE:${PN} = "usb-gadget-net.service usb-gadget-net-dhcp.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/usb-gadget-net ${D}${sbindir}/usb-gadget-net

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${UNPACKDIR}/usb-gadget-net.default ${D}${sysconfdir}/default/usb-gadget-net

    install -d ${D}${sysconfdir}/usb-gadget-net
    install -m 0644 ${UNPACKDIR}/udhcpd.conf ${D}${sysconfdir}/usb-gadget-net/udhcpd.conf

    install -d ${D}${sysconfdir}/NetworkManager/conf.d
    install -m 0644 ${UNPACKDIR}/99-usb-gadget-net-unmanaged.conf \
        ${D}${sysconfdir}/NetworkManager/conf.d/99-usb-gadget-net-unmanaged.conf

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/usb-gadget-net.service ${D}${systemd_system_unitdir}/
    install -m 0644 ${UNPACKDIR}/usb-gadget-net-dhcp.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/usb-gadget-net.service \
    ${systemd_system_unitdir}/usb-gadget-net-dhcp.service \
    ${sysconfdir}/NetworkManager/conf.d/99-usb-gadget-net-unmanaged.conf \
"
CONFFILES:${PN} = " \
    ${sysconfdir}/default/usb-gadget-net \
    ${sysconfdir}/usb-gadget-net/udhcpd.conf \
"

COMPATIBLE_MACHINE = "(imx95-navqdesktop|imx95)"
