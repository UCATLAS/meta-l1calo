DESCRIPTION = "Run opc server on boot of the board"
SRC_URI = "\
  file://run-init-opc-server.sh \
  file://LICENSE \
  file://init-opc-server.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=4d9db4b2970e8185b1a8c2e4dd416c7a"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-opc-server.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "python3-core gfex-opcua-server-milkyway"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${GFEX_PROGRAMS_DIR}
  install -m 0755 ${WORKDIR}/run-init-opc-server.sh ${D}${GFEX_PROGRAMS_DIR}/run-init-opc-server.sh
  
  # Install systemd service
  install -d ${D}${systemd_system_unitdir}
  install -m 0644 ${WORKDIR}/init-opc-server.service ${D}${systemd_system_unitdir}/init-opc-server.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  ${systemd_system_unitdir}/init-opc-server.service \
"
