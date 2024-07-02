DESCRIPTION = "Load user firmware on zFPGA"
SRC_URI:gfex-production = "\
  file://load-firmware.py \
  file://zfpga_top.bit \
  file://LICENSE \
"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;beginline=1;endline=18;md5=470c8811ac7dbd16d782e1422760fad8"

COMPATIBLE_MACHINE = "gfex-prototype4"

# these 3 lines will have the script run on boot
inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME = "load-firmware.py"
INITSCRIPT_PARAMS = "defaults 01"

RDEPENDS:${PN} = "python3-core"

# install it in the correct location for update-rc.d
do_install() {
  install -d ${D}${INIT_D_DIR}
  install -m 0755 ${WORKDIR}/load-firmware.py ${D}${INIT_D_DIR}/load-firmware.py
  install -d ${D}/lib/firmware
  install -m 0755 ${WORKDIR}/zfpga_top.bit ${D}/lib/firmware/zfpga_top.bit
}

# package it as it is not installed in a standard location
FILES:${PN} = "\
  ${INIT_D_DIR}/load-firmware.py \
  /lib/firmware/zfpga_top.bit \
"
