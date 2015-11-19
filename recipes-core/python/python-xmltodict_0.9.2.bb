DESCRIPTION = "Makes working with XML feel like you are working with JSON"
HOMEPAGE = "https://github.com/martinblech/xmltodict"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01441d50dc74476db58a41ac10cb9fa2"

SRC_URI = "https://pypi.python.org/packages/source/x/xmltodict/xmltodict-${PV}.tar.gz"
SRC_URI[md5sum] = "ab17e53214a8613ad87968e9674d75dd"
SRC_URI[sha256sum] = "275d1e68c95cd7e3ee703ddc3ea7278e8281f761680d6bdd637bcd00a5c59901"

S = "${WORKDIR}/xmltodict-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core"
