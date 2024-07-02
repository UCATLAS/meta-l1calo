# Have bitbake include ${THISDIR}/files to the list of search paths
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SYSTEM_USER_DTSI:gfex-production-p1 ?= "gfex-production.dtsi"
SYSTEM_USER_DTSI:gfex-production-stf ?= "gfex-production.dtsi"

SRC_URI:append = " file://${SYSTEM_USER_DTSI}"

do_configure:append() {
	cp ${WORKDIR}/${SYSTEM_USER_DTSI} ${B}/device-tree
	echo "/include/ \"${SYSTEM_USER_DTSI}\"" >> ${B}/device-tree/system-top.dts
}
		
