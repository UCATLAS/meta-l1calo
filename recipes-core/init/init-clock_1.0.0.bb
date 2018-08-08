# see https://stackoverflow.com/a/40768781
DESCRIPTION = "Clock Configuration via I2C"
SRC_URI_gfex-prototype4 = "\
  file://init-clock \
  file://init_clock/*\
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init-clock;beginline=2;endline=19;md5=846eef20187f1d9f7f2af0d254faa171"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "init-clock"

RDEPENDS_${PN} = "python-core python-fcntl python-periphery"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/init-clock ${D}${INIT_D_DIR}/init-clock
  install -d ${D}${INIT_D_DIR}/init_clock
  install -m 0755 ${WORKDIR}/init_clock/* ${D}${INIT_D_DIR}/init_clock/.
}

# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/init-clock \
  ${INIT_D_DIR}/init_clock \
"
