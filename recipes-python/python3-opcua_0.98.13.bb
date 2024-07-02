SUMMARY = "Pure Python OPC-UA client and server library"
HOMEPAGE = "https://pypi.org/project/opcua/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://opcua.egg-info/PKG-INFO;md5=772afb09a1612e16ca6b1d0e024b4936"

SRC_URI = "https://files.pythonhosted.org/packages/77/b1/5788cf02d4527ec816998ecd8b066242877f1b1ca6fcc07d7d4c0317fd04/opcua-0.98.13.tar.gz"
SRC_URI[md5sum] = "0a0d8e47c78d667c3b15ea1cfbdedc85"
SRC_URI[sha256sum] = "3352f30b5fed863146a82778aaf09faa5feafcb9dd446a4f49ff34c0c3ebbde6"

S = "${WORKDIR}/opcua-0.98.13"

RDEPENDS:${PN} = "${PYTHON_PN}-lxml ${PYTHON_PN}-pytz ${PYTHON_PN}-dateutil"

PYPI_PACKAGE = "opcua"

inherit pypi setuptools3
