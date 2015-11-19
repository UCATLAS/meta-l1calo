DESCRIPTION = "Python declarative parser for binary data"
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = ""
LIC_FILES_CHKSUM = "file://LICENSE;md5=f511459d8aaff1dc0bbc45558a61bea7"
DEPENDS = "python-twisted python-construct python-xmltodict python-zopeinterface"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = ""
SRC_URI[sha256sum] = ""

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted python-construct python-xmltodict python-zopeinterface"

