inherit setuptools
require python-periphery.inc

SRC_URI[md5sum] = "d1f876c49b54fc65012a6d5c9ddeb0d7"
SRC_URI[sha256sum] = "be32420bc831bd22871cc7c135c108318f8c684fd1e52213ab1168bf09e43677"

RDEPENDS_${PN} += "python-core python-fcntl python-ctypes python-mmap"
