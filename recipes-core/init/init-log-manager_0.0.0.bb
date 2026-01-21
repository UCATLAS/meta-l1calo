DESCRIPTION = "Manage gFEX log file size "
SRC_URI = "\
  file://run-init-log-manager.sh \
  file://log-manager.py \
  file://LICENSE \
  file://init-log-manager.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=b1aa6d414216b4e9f3ad6f491d8e4f70"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-log-manager.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-schedule"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${GFEX_PROGRAMS_DIR}
  install -m 0755 ${WORKDIR}/run-init-log-manager.sh ${D}${GFEX_PROGRAMS_DIR}/run-init-log-manager.sh
  
  install -d ${D}/software/misc
  install -m 0755 ${WORKDIR}/log-manager.py ${D}/software/misc/log-manager.py
  
  # Install systemd service
  install -d ${D}${systemd_system_unitdir}
  install -m 0644 ${WORKDIR}/init-log-manager.service ${D}${systemd_system_unitdir}/init-log-manager.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  /software/misc/log-manager.py \
  ${systemd_system_unitdir}/init-log-manager.service \
"
