# see https://stackoverflow.com/a/40768781
DESCRIPTION = "Clock Configuration via I2C"
SRC_URI:gfex-prototype4 = "\
  file://init-clock \
  file://si5345_GF240280-Registers.txt \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init-clock;beginline=2;endline=19;md5=e740893593d6ecb2033496b85b82c742"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "init-clock"
INITSCRIPT_PARAMS = "defaults 20"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-periphery"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/init-clock ${D}${INIT_D_DIR}/init-clock
  install -m 0755 ${WORKDIR}/si5345_GF240280-Registers.txt ${D}${INIT_D_DIR}/si5345_GF240280-Registers.txt
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${INIT_D_DIR}/init-clock \
  ${INIT_D_DIR}/si5345_GF240280-Registers.txt \
"
