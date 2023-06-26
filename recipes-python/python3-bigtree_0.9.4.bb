SUMMARY = "BigTree - Tree Implementation for Python, integrated with Python list, dictionary, and pandas DataFrame."
HOMEPAGE = "https://github.com/kayjan/bigtree"
AUTHOR = "Kay Jan"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6a46bf0dd4463bb3d7079f1e625d881e"

PYPI_PACKAGE = "bigtree"

inherit pypi setuptools3

SRC_URI = "https://files.pythonhosted.org/packages/a3/06/d26933e315119b44cb2b7cfcfde9085578c4bfefd8991b2f226ab875ec4e/bigtree-0.9.4.tar.gz"

SRC_URI[md5sum] = "ff93e5bfaf3cd40c07a9bf4c9c0c0b34"
SRC_URI[sha256sum] = "37df7ef5b31bb3d81c09ee950b0fd208446fc2f20a2ad9787826deecda68e821"

RDEPENDS_${PN} = "python3-pandas"


