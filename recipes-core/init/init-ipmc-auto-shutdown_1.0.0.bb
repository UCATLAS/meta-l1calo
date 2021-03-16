DESCRIPTION = "Automatic Shutdown Based on Temperature Sensor Readings"
SRC_URI_gfex-prototype4 = "\
  file://run-init-ipmc-auto-shutdown.sh \
  file://init_ipmc_auto_shutdown/* \
  file://gpio-dev-mem-test.c \
"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/init_ipmc_auto_shutdown/gpio_all_sensor_autoshutdown;beginline=2;endline=19;md5=6f485c8366ff2b4b2fcaa5d957ea761a"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "run-init-ipmc-auto-shutdown.sh"

RDEPENDS_${PN} = "python-core python-fcntl python-periphery"

#Compile the C script
do_compile() {
         ${CC} ${WORKDIR}/gpio-dev-mem-test.c -o gpio-dev-mem-test
}

# install it in the correct location for update-rc.d
do_install() {
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${WORKDIR}/run-init-ipmc-auto-shutdown.sh ${D}${INIT_D_DIR}/run-init-ipmc-auto-shutdown.sh
    install -d ${D}${INIT_D_DIR}/init_ipmc_auto_shutdown
    install -m 0755 ${WORKDIR}/init_ipmc_auto_shutdown/* ${D}${INIT_D_DIR}/init_ipmc_auto_shutdown/.
    install -m 0755 gpio-dev-mem-test ${D}${INIT_D_DIR}/init_ipmc_auto_shutdown/
}

#  ${INIT_D_DIR}/init-auto-shutdown \
# package it as it is not installed in a standard location
FILES_${PN} = "\
  ${INIT_D_DIR}/run-init-ipmc-auto-shutdown.sh \
  ${INIT_D_DIR}/init_ipmc_auto_shutdown \
  ${INIT_D_DIR}/gpio-dev-mem-test \
"

INSANE_SKIP_${PN} = "ldflags"