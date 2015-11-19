DESCRIPTION = "Python declarative parser for binary data"
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = ""
LIC_FILES_CHKSUM = "file://LICENSE;md5=f511459d8aaff1dc0bbc45558a61bea7"
DEPENDS = "python-twisted python-construct python-xmltodict python-zopeinterface"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = "1132a46ab870cd0bf5281bb7b7ad75e1"
SRC_URI[sha256sum] = "7eafab801f4ece6997f6482e6fe658376f06e394d540c905e48ab8e174ee2e39"

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted python-construct python-xmltodict python-zopeinterface"

