SUMMARY = "Scripts used in the setup and testing of gFEX production board"
HOMEPAGE = "https://gitlab.cern.ch/atlas-l1calo/gfex/gfex-management-scripts"
AUTHOR = "Emily Smith <emsmith@cern.ch>"

SRC_URI = "git://git@gitlab.cern.ch:7999/atlas-l1calo/gfex/gfex-management-scripts.git;branch=master;protocol=ssh"
SRCREV ?= "21de38d15a0ac37fe2613502670fc4b3622e3cba"

S = "${WORKDIR}/git"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

RDEPENDS:${PN} = "python3-numpy python3-pyyaml python3-periphery"

do_install () {
	   install -d ${D}/software/gfex-management-scripts
	   cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}/software/gfex-management-scripts/
}

PACKAGES = "${PN}"
FILES:${PN}="/software/gfex-management-scripts"