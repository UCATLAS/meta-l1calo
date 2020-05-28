DESCRIPTION = "A pure Python 2/3 library for peripheral I/O (GPIO, LED, PWM, SPI, I2C, MMIO, Serial) in Linux."
HOMEPAGE = "http://pythonhosted.org/python-periphery/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=67423f80c79796aa19cd3a487b42ee0b"

SRC_URI[md5sum] = "1d958f02575d4a19734ee2dd92336157"
SRC_URI[sha256sum] = "5da4d5f40ff8974cf6c724587baa674d7e0593f07b6f6ee896104f11c1be18ec"

inherit setuptools3 pypi

PYPI_PACKAGE = "python-periphery"

RDEPENDS_${PN} += "python-core python-fcntl python-ctypes python-mmap"
