DESCRIPTION = "Run opc server on boot of the board"
SRC_URI_gfex-production = "\
  file://run-init-opc-server.sh \
  file://LICENSE \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=4d9db4b2970e8185b1a8c2e4dd416c7a"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-opc-server.sh"

RDEPENDS_${PN} = "python3-core gfex-opcua-server-milkyway"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-opc-server.sh ${D}${INIT_D_DIR}/run-init-opc-server.sh
}

# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/run-init-opc-server.sh \
"
