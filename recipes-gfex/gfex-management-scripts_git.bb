SUMMARY = "Scripts used in the setup and testing of gFEX production board"
HOMEPAGE = "https://gitlab.cern.ch/atlas-l1calo/gfex/gfex-management-scripts"
AUTHOR = "Emily Smith <emsmith@cern.ch>"

SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-l1calo/gfex/gfex-management-scripts.git;branch=master;protocol=ssh"
SRCREV ?= "b490866fbf134b95b74b7b38fb55d531c285328d"

S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=f7ea1d315b42c9b3f9debf6fd41e5dca"

RDEPENDS:${PN} = "python3-numpy python3-pyyaml python3-periphery"

do_install () {
	   install -d ${D}/software/gfex-management-scripts
	   cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}/software/gfex-management-scripts/
}

PACKAGES = "${PN}"
FILES:${PN}="/software/gfex-management-scripts"