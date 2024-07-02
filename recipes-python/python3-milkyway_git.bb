SUMMARY = "Pythonic OPCUA Server"
HOMEPAGE = "https://gitlab.cern.ch/quasar-team/MilkyWay"
AUTHOR = "Piotr Nikiel"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://NOTES.txt;md5=85db315b1d346827e09dcf1107be4497"

SRC_URI = "git://git@gitlab.cern.ch:7999/quasar-team/MilkyWay.git;branch=master;protocol=ssh"
SRCREV ?= "dad44fc7d643a0d60609ce5b323e7fa849f7f3df"

S = "${WORKDIR}/git"

RDEPENDS:${PN} = "python3-opcua"

do_install () {
           install -d ${D}/usr/lib/python3.7/site-packages/MilkyWay
	   cp -R --no-dereference --preserve=mode,links -v ${S}/* ${D}/usr/lib/python3.7/site-packages/MilkyWay/
}

PACKAGES = "${PN}"
FILES:${PN}="/usr/lib/python3.7/site-packages/MilkyWay"