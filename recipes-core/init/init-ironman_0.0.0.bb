DESCRIPTION = "Repeated polling of i2c sensor values"
SRC_URI_gfex-production = "\
  file://run-init-ironman.sh \
  file://LICENSE \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=b3bb4917b02e28aa18f38fa79ccb195c"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-ironman.sh"

RDEPENDS_${PN} = "python3-core python3-ironman gfex-register-access"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-ironman.sh ${D}${INIT_D_DIR}/run-init-ironman.sh
}

# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/run-init-ironman.sh \
"
