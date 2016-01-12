DESCRIPTION = "Ironman is a general purpose software toolbox to be run on L1Calo hardware with embedded processors (SoCs)."
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = "3b9d625835d1be95498337b914dae79a"
SRC_URI[sha256sum] = "705cc7ff2adbedd9b2092b3b94c7a3f689a638557fdc3b2586561cf8f7a34144"

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface"
PREFERRED_VERSION_python-pyyaml = "3.11"
