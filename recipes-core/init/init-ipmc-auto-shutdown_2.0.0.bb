DESCRIPTION = "Automatic Shutdown Based on Temperature Sensor Readings"
SRC_URI:gfex-prototype4 = "\
  file://run-init-ipmc-auto-shutdown.sh \
  file://ipmc_auto_shutdown/gpio-dev-mem-test \
  file://ipmc_auto_shutdown/gpio_all_sensor_autoshutdown.py \
"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/ipmc_auto_shutdown/gpio_all_sensor_autoshutdown.py;beginline=2;endline=19;md5=6f485c8366ff2b4b2fcaa5d957ea761a"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-ipmc-auto-shutdown.sh"
INITSCRIPT_PARAMS = "defaults 99"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-periphery"

# install it in the correct location for update-rc.d
do_install() {
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/run-init-ipmc-auto-shutdown.sh ${D}${INIT_D_DIR}/run-init-ipmc-auto-shutdown.sh
    install -d ${D}/software/misc/ipmc_auto_shutdown
    install -m 0755 ${WORKDIR}/ipmc_auto_shutdown/gpio-dev-mem-test ${D}/software/misc/ipmc_auto_shutdown/.
    install -m 0755 ${WORKDIR}/ipmc_auto_shutdown/gpio_all_sensor_autoshutdown.py ${D}/software/misc/ipmc_auto_shutdown/.
}

#  ${INIT_D_DIR}/init-auto-shutdown \
# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${INIT_D_DIR}/run-init-ipmc-auto-shutdown.sh \
  /software/misc/ipmc_auto_shutdown \
"

INSANE_SKIP_${PN} = "ldflags"
