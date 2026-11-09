SUMMARY = "Start the flightstack-uploader at boot with the Skynode directory layout"
DESCRIPTION = "NavQ95 counterpart of AuterionOS flightstack-updater.service and \
run_flightstack_updater.sh. Creates /shared_container_dir/fmu and /persistent \
(tmpfiles.d) and runs the uploader from a manually copied checkout \
(/home/user/px4/flightstack-uploader by default) with DEVICE=navq95. \
The uploader itself is not part of the image."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://run_flightstack_updater.sh \
    file://flightstack-updater.default \
    file://flightstack-updater.service \
    file://flightstack-updater.tmpfiles \
"

inherit systemd
SYSTEMD_SERVICE:${PN} = "flightstack-updater.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/run_flightstack_updater.sh ${D}${sbindir}/run_flightstack_updater.sh

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${UNPACKDIR}/flightstack-updater.default ${D}${sysconfdir}/default/flightstack-updater

    install -d ${D}${nonarch_libdir}/tmpfiles.d
    install -m 0644 ${UNPACKDIR}/flightstack-updater.tmpfiles ${D}${nonarch_libdir}/tmpfiles.d/flightstack-updater.conf

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/flightstack-updater.service ${D}${systemd_system_unitdir}/
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/flightstack-updater.service \
    ${nonarch_libdir}/tmpfiles.d/flightstack-updater.conf \
"
CONFFILES:${PN} = "${sysconfdir}/default/flightstack-updater"

COMPATIBLE_MACHINE = "(imx95-navqdesktop|imx95)"
