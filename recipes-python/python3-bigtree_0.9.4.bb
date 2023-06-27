SUMMARY = "BigTree - Tree Implementation for Python, integrated with Python list, dictionary, and pandas DataFrame."
HOMEPAGE = "https://github.com/kayjan/bigtree"
AUTHOR = "Kay Jan"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d17cca707393ef99291b58377ab60797"

PYPI_PACKAGE = "bigtree"

inherit pypi setuptools3

SRC_URI = "https://files.pythonhosted.org/packages/a3/06/d26933e315119b44cb2b7cfcfde9085578c4bfefd8991b2f226ab875ec4e/bigtree-0.9.4.tar.gz"

SRC_URI[md5sum] = "ff93e5bfaf3cd40c07a9bf4c9c0c0b34"
SRC_URI[sha256sum] = "37df7ef5b31bb3d81c09ee950b0fd208446fc2f20a2ad9787826deecda68e821"

RDEPENDS_${PN} = "python3-pandas"

do_configure_prepend() {
cat > ${S}/setup.py <<-EOF
from setuptools import setup, find_packages

setup(
       name="${PYPI_PACKAGE}",
       version="${PV}",
       license="${LICENSE}",
       packages=find_packages(),    
       install_requires=['numpy >= 1.17.0', 'pandas >= 0.23.4'],
)
EOF
}
