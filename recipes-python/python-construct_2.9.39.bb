inherit setuptools
require python-construct.inc

SRC_URI[md5sum] = "39407f91407138f687ea2ecf8d758a1f"
SRC_URI[sha256sum] = "c23ef47fd5751d821e5c17d9b3b02d1f3c465e66b8a205b9c791c4abef747138"

RDEPENDS_${PN} += "python-core python-six python-debugger"
