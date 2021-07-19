DESCRIPTION = "Automatic Shutdown Based on Temperature Sensor Readings"
SRC_URI_gfex-prototype4 = "\
  file://run-init-auto-shutdown.sh \
  file://init_auto_shutdown/* \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init_auto_shutdown/init-auto-shutdown;beginline=2;endline=19;md5=688775089a7d513a2ae8a943a86d1f56"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-auto-shutdown.sh"

RDEPENDS_${PN} = "python3-core python3-fcntl python3-periphery"

#  install -m 0755 ${WORKDIR}/init-auto-shutdown ${D}${INIT_D_DIR}/init-auto-shutdown
# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-auto-shutdown.sh ${D}${INIT_D_DIR}/run-init-auto-shutdown.sh
  install -d ${D}${INIT_D_DIR}/init_auto_shutdown
  install -m 0755 ${WORKDIR}/init_auto_shutdown/* ${D}${INIT_D_DIR}/init_auto_shutdown/.
}

#  ${INIT_D_DIR}/init-auto-shutdown \
# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/run-init-auto-shutdown.sh \
  ${INIT_D_DIR}/init_auto_shutdown \
"
