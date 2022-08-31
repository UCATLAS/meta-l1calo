SUMMARY = "Job scheduling for humans."
HOMEPAGE = "https://pypi.org/project/schedule/"

LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=6400f153491d45ea3459761627ca24b2"

#SRC_URI = "https://files.pythonhosted.org/packages/4c/c4/13b4776ea2d76c115c1d1b84579f3764ee6d57204f6be27119f13a61d0a9/python-dateutil-2.8.2.tar.gz"
SRC_URI[md5sum] = "9bf7544e37824e450457187633a17b17"
SRC_URI[sha256sum] = "e6ca13585e62c810e13a08682e0a6a8ad245372e376ba2b8679294f377dfc8e4"

S = "${WORKDIR}/python-schedule-1.1.0/"

DEPENDS += "${PYTHON_PN}-setuptools-scm-native"

PYPI_PACKAGE = "schedule"

inherit pypi setuptools3
