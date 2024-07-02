SUMMARY = "Register Access for gFEX"
HOMEPAGE = "https://github.com/kratsg/ironman"
AUTHOR = "Emily Smith <emsmith@cern.ch>"

SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-l1calo/gfex/gfex-register-access.git;branch=master;protocol=ssh"
SRCREV ?= "a1691e4a5c91d63ee8fad80dff8060b9a7231897"

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
