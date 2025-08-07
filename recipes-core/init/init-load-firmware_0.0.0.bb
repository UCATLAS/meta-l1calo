DESCRIPTION = "Load user firmware on zFPGA"
SRC_URI = "\
  file://load-firmware.py \
  file://zfpga_top.bit \
  file://LICENSE \
  file://init-load-firmware.service \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=470c8811ac7dbd16d782e1422760fad8"

COMPATIBLE_MACHINE = "(gfex-prototype4|gfex-production-stf|gfex-production-p1)"

# these 3 lines will have the script run on boot
inherit systemd
SYSTEMD_SERVICE:${PN} = "init-load-firmware.service"
GFEX_PROGRAMS_DIR = "${libdir}/gfex-programs"

RDEPENDS:${PN} = "python3-core"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${GFEX_PROGRAMS_DIR}
  install -m 0755 ${WORKDIR}/load-firmware.py ${D}${GFEX_PROGRAMS_DIR}/load-firmware.py
  
  install -d ${D}/usr/firmware
  install -m 0755 ${WORKDIR}/zfpga_top.bit ${D}/usr/firmware/zfpga_top.bit
  
  # Install systemd service
  install -d ${D}${systemd_system_unitdir}
  install -m 0644 ${WORKDIR}/init-load-firmware.service ${D}${systemd_system_unitdir}/init-load-firmware.service
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${GFEX_PROGRAMS_DIR} \
  /usr/firmware/zfpga_top.bit \
  ${systemd_system_unitdir}/init-load-firmware.service \
"
