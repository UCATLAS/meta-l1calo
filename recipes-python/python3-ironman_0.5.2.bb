
SUMMARY = "SoC Firmware for L1Calo"
HOMEPAGE = "https://github.com/kratsg/ironman"
AUTHOR = "Giordon Stark, Emily Smith <gstark@cern.ch, emsmith@cern.ch>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

SRC_URI = "https://files.pythonhosted.org/packages/3b/f9/5a241ca0f4e244a3d29a1a4c53af138ff8d0d6e8b21747104ec7316d47c1/ironman-0.5.2.tar.gz"
SRC_URI[md5sum] = "55cf93c8ca97298b80b3778b61483b38"
SRC_URI[sha256sum] = "d339349f1a51a515748da064649ce6a46a77defbe541318d018a3cefba5b0188"

S = "${WORKDIR}/ironman-0.5.2"

RDEPENDS_${PN} = "python3-twisted python3-zopeinterface python3-construct python3-pyyaml"

PYPI_PACKAGE = "ironman"

inherit setuptools3
