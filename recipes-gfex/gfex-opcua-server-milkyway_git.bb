SUMMARY = "gFEX OPCUA Python Server"
HOMEPAGE = "https://gitlab.cern.ch/atlas-l1calo/gfex/opc-ua-gfex-milkyway-server"
AUTHOR = "Emily Smith <emsmith@cern.ch>"

SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-l1calo/gfex/opc-ua-gfex-milkyway-server.git;branch=master;protocol=ssh"
SRCREV ?= "19bb29d87fa7f1107cc27d05168c42b376ae4028"
S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=4d9db4b2970e8185b1a8c2e4dd416c7a"

#PV = "1.0+git${SRCPV}"

RDEPENDS:${PN} = "python3-milkyway"

do_install () {
	   install -d ${D}/software/opc-ua-gfex-milkyway-server/
	   cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}/software/opc-ua-gfex-milkyway-server/
}

PACKAGES = "${PN}"
FILES:${PN}="/software/opc-ua-gfex-milkyway-server/"
