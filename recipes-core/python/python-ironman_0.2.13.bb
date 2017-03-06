DESCRIPTION = "Ironman is a general purpose software toolbox to be run on L1Calo hardware with embedded processors (SoCs)."
HOMEPAGE = "https://iron-man.readthedocs.org/en/latest/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

SRC_URI = "https://pypi.python.org/packages/source/i/ironman/ironman-${PV}.tar.gz"
SRC_URI[md5sum] = "a9b0816b95544d8fb1db30fce000a8e8"
SRC_URI[sha256sum] = "c57e02967b0a8e080bd9849c3c4c639846c1988e8255f22beba3d94209b0a323"

S = "${WORKDIR}/ironman-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface"
PREFERRED_VERSION_python-pyyaml = "3.10"
FILES_${PN} += " \
    /usr/share/workbench/gFEXTest.py \
    /usr/share/workbench/xadc.yml \
"
