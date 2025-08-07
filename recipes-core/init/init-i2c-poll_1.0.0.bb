DESCRIPTION = "Repeated polling of i2c sensor values"
SRC_URI = "\
  file://run-init-i2c-poll.sh \
  file://i2c_poll/init-i2c-poll.py \
  file://i2c_poll/gfex_temperature.py \
  file://i2c_poll/gfex_power.py \
  file://i2c_poll/gfex_minipods.py \
  file://init-i2c-poll.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/i2c_poll/init-i2c-poll.py;beginline=2;endline=19;md5=6f485c8366ff2b4b2fcaa5d957ea761a"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-i2c-poll.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-periphery"

do_install() {
  install -d ${D}${GFEX_PROGRAMS_DIR}
  install -m 0755 ${WORKDIR}/run-init-i2c-poll.sh ${D}${GFEX_PROGRAMS_DIR}/run-init-i2c-poll.sh
  install -d ${D}/software/misc/i2c_poll
  install -m 0755 ${WORKDIR}/i2c_poll/* ${D}/software/misc/i2c_poll/.
  
  # Install systemd service
  install -d ${D}${systemd_system_unitdir}
  install -m 0644 ${WORKDIR}/init-i2c-poll.service ${D}${systemd_system_unitdir}/init-i2c-poll.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  /software/misc/i2c_poll/* \
  ${systemd_system_unitdir}/init-i2c-poll.service \
"
