DESCRIPTION = "Repeated polling of i2c sensor values"
SRC_URI:gfex-production = "\
  file://run-init-resize-rootfs.sh \
  file://LICENSE \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=470c8811ac7dbd16d782e1422760fad8"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-resize-rootfs.sh"
INITSCRIPT_PARAMS = "defaults 10"

RDEPENDS:${PN} = "e2fsprogs-resize2fs bash"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-resize-rootfs.sh ${D}${INIT_D_DIR}/run-init-resize-rootfs.sh
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${INIT_D_DIR}/run-init-resize-rootfs.sh \
"
