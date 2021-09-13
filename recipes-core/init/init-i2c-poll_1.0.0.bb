DESCRIPTION = "Repeated polling of i2c sensor values"
SRC_URI_gfex-prototype4 = "\
  file://run-init-i2c-poll.sh \
  file://init_i2c_poll/* \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init_i2c_poll/init-i2c-poll;beginline=2;endline=19;md5=6f485c8366ff2b4b2fcaa5d957ea761a"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-i2c-poll.sh"

RDEPENDS_${PN} = "python3-core python3-fcntl python3-periphery"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/run-init-i2c-poll.sh ${D}${INIT_D_DIR}/run-init-i2c-poll.sh
  install -d ${D}${INIT_D_DIR}/init_i2c_poll
  install -m 0755 ${WORKDIR}/init_i2c_poll/* ${D}${INIT_D_DIR}/init_i2c_poll/.
}

#  ${INIT_D_DIR}/init-i2c-poll \
# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/run-init-i2c-poll.sh \
  ${INIT_D_DIR}/init_i2c_poll/* \
"
