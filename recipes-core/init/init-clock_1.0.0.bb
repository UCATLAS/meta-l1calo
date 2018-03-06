# see https://stackoverflow.com/a/40768781
DESCRIPTION = "Clock Configuration via I2C"
SRC_URI = "file://init-clock"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init-clock;beginline=2;endline=19;md5=846eef20187f1d9f7f2af0d254faa171"

# these 4 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "init-clock"

do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/init-clock ${D}${INIT_D_DIR}/init-clock
}

# package it as it is not installed in a standard location
FILES_${PN} = "${INIT_D_DIR}/init-clock"
