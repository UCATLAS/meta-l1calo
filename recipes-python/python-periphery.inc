DESCRIPTION = "A pure Python 2/3 library for peripheral I/O (GPIO, LED, PWM, SPI, I2C, MMIO, Serial) in Linux."
HOMEPAGE = "http://pythonhosted.org/python-periphery/"
SECTION = "devel/python"
LICENSE = "MIT"
# NB: not a real license file, the actual one is located at
# https://raw.githubusercontent.com/vsergeev/python-periphery/v${PV}/LICENSE
LIC_FILES_CHKSUM = "file://PKG-INFO;md5=6c22d44aeea7782a0f1c623e8dd10233"

SRC_URI[md5sum] = "d1f876c49b54fc65012a6d5c9ddeb0d7"
SRC_URI[sha256sum] = "be32420bc831bd22871cc7c135c108318f8c684fd1e52213ab1168bf09e43677"

PYPI_PACKAGE = "python-periphery"
inherit pypi
