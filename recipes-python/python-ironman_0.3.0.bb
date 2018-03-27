inherit setuptools
require python-ironman.inc

SRC_URI[md5sum] = "f75232cbb7186d0a8babf2898a005cc4"
SRC_URI[sha256sum] = "0f5f9b9355e08792c14180dc4743c2c1c4bdf0840367283512311410dcc6e61f"

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface"
PREFERRED_VERSION_python-pyyaml = "3.10"
FILES_${PN} += " \
    /usr/share/workbench/gFEXTest.py \
    /usr/share/workbench/xadc.yml \
"
