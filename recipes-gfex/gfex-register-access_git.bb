SUMMARY = "Register Access for gFEX"
HOMEPAGE = "https://gitlab.cern.ch/atlas-l1calo/gfex/gfex-register-access"
AUTHOR = "Emily Smith <emsmith@cern.ch>"

SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-l1calo/gfex/gfex-register-access.git;branch=master;protocol=ssh"
SRCREV ?= "a3e2621cfa9e42ca070719eaf511de8e69413d55"

S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/ReadRegisters.py;beginline=2;endline=20;md5=084a7fe9ef8dc0bb313619ce08334403"

#PV = "1.0+git${SRCPV}"

RDEPENDS:${PN} = "python3-numpy python3-pyyaml python3-periphery"

do_install () {
	   install -d ${D}/software/gfex-register-access
	   cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}/software/gfex-register-access/
}

PACKAGES = "${PN}"
FILES:${PN}="/software/gfex-register-access"
