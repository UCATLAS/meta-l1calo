# see https://stackoverflow.com/a/40768781
DESCRIPTION = "Clock Configuration via I2C"
SRC_URI = "\
  file://init-clock \
  file://si5345_GF240280-Registers.txt \
  file://init-clock.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init-clock;beginline=2;endline=19;md5=e740893593d6ecb2033496b85b82c742"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-clock.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-periphery"

# install it in the correct location for update-rc.d
do_install() {
    # Install script into /usr/lib/gfex-programs
    install -d ${D}${GFEX_PROGRAMS_DIR}
    install -m 0755 ${WORKDIR}/init-clock ${D}${GFEX_PROGRAMS_DIR}/init-clock
    
    # Install config file /usr/lib/gfex-programs/init-clock-config
    install -d ${D}${GFEX_PROGRAMS_DIR}/init-clock-config
    install -m 0644 ${WORKDIR}/si5345_GF240280-Registers.txt ${D}${GFEX_PROGRAMS_DIR}/init-clock-config/si5345_GF240280-Registers.txt

    # Install systemd service
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/init-clock.service ${D}${systemd_system_unitdir}/init-clock.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
    ${GFEX_PROGRAMS_DIR} \
    ${systemd_system_unitdir}/init-clock.service \
"
