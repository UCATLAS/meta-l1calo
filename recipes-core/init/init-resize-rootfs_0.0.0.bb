DESCRIPTION = "Repeated polling of i2c sensor values"
SRC_URI = "\
  file://run-init-resize-rootfs.sh \
  file://LICENSE \
  file://init-resize-rootfs.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=470c8811ac7dbd16d782e1422760fad8"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-resize-rootfs.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "e2fsprogs-resize2fs bash"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${GFEX_PROGRAMS_DIR}
  install -m 0755 ${WORKDIR}/run-init-resize-rootfs.sh ${D}${GFEX_PROGRAMS_DIR}/run-init-resize-rootfs.sh
  
  # Install systemd service
  install -d ${D}${systemd_system_unitdir}
  install -m 0644 ${WORKDIR}/init-resize-rootfs.service ${D}${systemd_system_unitdir}/init-resize-rootfs.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  ${systemd_system_unitdir}/init-resize-rootfs.service \
"
