DESCRIPTION = "Ironman is a general purpose software toolbox to be run on L1Calo hardware with embedded processors (SoCs)."
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = "9f012cc7a33f3a73d8608c0d103c5789"
SRC_URI[sha256sum] = "a5e2ce345f733bfd6331bca7801c1b222972c880a515d524ed2075a78c58303c"

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted python-construct python-xmltodict python-zopeinterface"

