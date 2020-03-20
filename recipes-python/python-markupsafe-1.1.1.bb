SUMMARY = "Add python markupsafe to python-native"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=ffeffa59c90c9c4a033c7574f8f3fb75"

SRC_URI[md5sum] = "43fd756864fe42063068e092e220c57b"
SRC_URI[sha256sum] = "29872e92839765e546828bb7754a68c418d927cd064fd4708fab9fe9c8bb116b	"	


BBCLASSEXTEND = "native"

inherit pypi setuptools

PYPI_PACKAGE = "MarkupSafe"