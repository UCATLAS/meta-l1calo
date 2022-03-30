SUMMARY = "gFEX OPCUA Python Server"
HOMEPAGE = "https://gitlab.cern.ch/atlas-l1calo/gfex/opc-ua-gfex-milkyway-server"
AUTHOR = "Emily Smith <emsmith@cern.ch>"

SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-l1calo/gfex/opc-ua-gfex-milkyway-server.git;branch=master;protocol=ssh"
SRCREV ?= "6efbe5e4f3286ae4c3f49dd98261393680a091f8"

S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=4d9db4b2970e8185b1a8c2e4dd416c7a"

#PV = "1.0+git${SRCPV}"

RDEPENDS_${PN} = "python3-milkyway"

do_install () {
	   install -d ${D}/software/opc-ua-gfex-milkyway-server/
	   cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}/software/opc-ua-gfex-milkyway-server/
}

PACKAGES = "${PN}"
FILES_${PN}="/software/opc-ua-gfex-milkyway-server/"