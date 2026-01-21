DESCRIPTION = "Run Startup Management Scripts at Boot"
SRC_URI = "\
  file://run-init-management-scripts.sh \
  file://LICENSE \
  file://init-management-scripts.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=f7ea1d315b42c9b3f9debf6fd41e5dca"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-management-scripts.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "gfex-management-scripts"

# install it in the correct location
do_install() {
  install -d ${D}${GFEX_PROGRAMS_DIR}
  install -m 0755 ${WORKDIR}/run-init-management-scripts.sh ${D}${GFEX_PROGRAMS_DIR}/run-init-management-scripts.sh
  
  # Install systemd service
  install -d ${D}${systemd_system_unitdir}
  install -m 0644 ${WORKDIR}/init-management-scripts.service ${D}${systemd_system_unitdir}/init-management-scripts.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  /software/gfex-management-scripts \
  ${systemd_system_unitdir}/init-management-scripts.service \
"