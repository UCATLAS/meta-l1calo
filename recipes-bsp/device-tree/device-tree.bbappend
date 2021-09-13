# Have bitbake include ${THISDIR}/files to the list of search paths
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SYSTEM_USER_DTSI_gfex-production-p1 ?= "gfex-production.dtsi"
SYSTEM_USER_DTSI_gfex-production-stf ?= "gfex-production.dtsi"

SRC_URI_append = " file://${SYSTEM_USER_DTSI}"

do_configure_append() {
	cp ${WORKDIR}/${SYSTEM_USER_DTSI} ${B}/device-tree
	echo "/include/ \"${SYSTEM_USER_DTSI}\"" >> ${B}/device-tree/system-top.dts
}
		
