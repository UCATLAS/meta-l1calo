DESCRIPTION = "Ironman is a general purpose software toolbox to be run on L1Calo hardware with embedded processors (SoCs)."
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = "a5766da69417bad21b1203ca20f2d7b1"
SRC_URI[sha256sum] = "65c787eb8900527559eaf217eff6ae85f54994b37f5d49ac02eca0887ce9e8e2"

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface"
PREFERRED_VERSION_python-pyyaml = "3.10"
FILES_${PN} = " \
    /usr/share/workbench/gFEXTest.py \
    /usr/share/workbench/xadc.yml \
"

