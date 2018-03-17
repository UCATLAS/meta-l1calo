DESCRIPTION = "Ironman is a general purpose software toolbox to be run on L1Calo hardware with embedded processors (SoCs)."
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = "1d13f30caa41c2061067da73ce6ce59c"
SRC_URI[sha256sum] = "2df34b2d2d2631c9fee40277186db1b7b1ac3e929439e8b8931bac38b9e10dd3"

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface"
PREFERRED_VERSION_python-pyyaml = "3.10"
