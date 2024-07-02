DESCRIPTION = "Repeated polling of i2c sensor values"
SRC_URI:gfex-prototype4 = "\
  file://run-init-i2c-poll.sh \
  file://i2c_poll/init-i2c-poll.py \
  file://i2c_poll/gfex_temperature.py \
  file://i2c_poll/gfex_power.py \
  file://i2c_poll/gfex_minipods.py \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/i2c_poll/init-i2c-poll.py;beginline=2;endline=19;md5=6f485c8366ff2b4b2fcaa5d957ea761a"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-i2c-poll.sh"
INITSCRIPT_PARAMS = "defaults 30"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-periphery"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-i2c-poll.sh ${D}${INIT_D_DIR}/run-init-i2c-poll.sh
  install -d ${D}/software/misc/i2c_poll
  install -m 0755 ${WORKDIR}/i2c_poll/* ${D}/software/misc/i2c_poll/.
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${INIT_D_DIR}/run-init-i2c-poll.sh \
  /software/misc/i2c_poll/* \
"
