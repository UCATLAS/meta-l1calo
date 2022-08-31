DESCRIPTION = "Manage gFEX log file size "
SRC_URI_gfex-prototype4 = "\
  file://run-init-log-manager.sh \
  file://log-manager.py \
  file://LICENSE \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=b1aa6d414216b4e9f3ad6f491d8e4f70"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-log-manager.sh"

RDEPENDS_${PN} = "python3-core python3-fcntl python3-schedule"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-log-manager.sh ${D}${INIT_D_DIR}/run-init-log-manager.sh
  install -d ${D}/software/misc
  install -m 0755 ${WORKDIR}/log-manager.py ${D}/software/misc/log-manager.py
}

# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/run-init-log-manager.sh \
  /software/misc/log-manager.py \
"
