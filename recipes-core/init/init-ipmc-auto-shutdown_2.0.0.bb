DESCRIPTION = "Automatic Shutdown Based on Temperature Sensor Readings"
SRC_URI = "\
  file://run-init-ipmc-auto-shutdown.sh \
  file://ipmc_auto_shutdown/gpio-dev-mem-test \
  file://ipmc_auto_shutdown/gpio_all_sensor_autoshutdown.py \
  file://init-ipmc-auto-shutdown.service \
"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/ipmc_auto_shutdown/gpio_all_sensor_autoshutdown.py;beginline=2;endline=19;md5=6f485c8366ff2b4b2fcaa5d957ea761a"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-ipmc-auto-shutdown.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "python3-core python3-fcntl python3-periphery"

# install it in the correct location for update-rc.d
do_install() {
    install -d ${D}${GFEX_PROGRAMS_DIR}
    install -m 0755 ${WORKDIR}/run-init-ipmc-auto-shutdown.sh ${D}${GFEX_PROGRAMS_DIR}/run-init-ipmc-auto-shutdown.sh
    install -d ${D}/software/misc/ipmc_auto_shutdown
    install -m 0755 ${WORKDIR}/ipmc_auto_shutdown/gpio-dev-mem-test ${D}/software/misc/ipmc_auto_shutdown/.
    install -m 0755 ${WORKDIR}/ipmc_auto_shutdown/gpio_all_sensor_autoshutdown.py ${D}/software/misc/ipmc_auto_shutdown/.
    
    # Install systemd service
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/init-ipmc-auto-shutdown.service ${D}${systemd_system_unitdir}/init-ipmc-auto-shutdown.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  /software/misc/ipmc_auto_shutdown \
  ${systemd_system_unitdir}/init-ipmc-auto-shutdown.service \
"

INSANE_SKIP_${PN} = "ldflags"
