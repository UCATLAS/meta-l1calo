DESCRIPTION = "Python declarative parser for binary data"
HOMEPAGE = "http://construct.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f511459d8aaff1dc0bbc45558a61bea7"
DEPENDS = "python python-six"

SRC_URI = "https://pypi.python.org/packages/source/c/construct/construct-${PV}.tar.gz"
SRC_URI[md5sum] = "75fc5e311ee5624e63268067e4b0d97b"
SRC_URI[sha256sum] = "665b6271eeadf15219c726b180c8d7a641d026784d72ca3dad90a20aae009020"

S = "${WORKDIR}/construct-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-lang python-shell python-six python-debugger"
