SUMMARY = "SoC Firmware for L1Calo"
HOMEPAGE = "https://github.com/kratsg/ironman"
AUTHOR = "Giordon Stark, Emily Smith <gstark@cern.ch, emsmith@cern.ch>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

PYPI_PACKAGE = "ironman"

inherit pypi setuptools3

SRC_URI = "https://files.pythonhosted.org/packages/63/df/b1dc2440c2a47d79fbfb4eaf5fa55979cf2bc3744380ca33a86f93a8c3cd/ironman-0.6.0.tar.gz"

SRC_URI[md5sum] = "f1fab976d2fd28613d0e919629d82169"
SRC_URI[sha256sum] = "6cc57944ac132988f19e74095d6425aefcbb58fc8a94f4654dcf48ec562692d2"

RDEPENDS:${PN} = "python3-twisted python3-zopeinterface python3-construct python3-pyyaml"
//PREFERRED_VERSION_python3-twisted = "22.2.0"

