inherit setuptools
require python-ironman.inc

SRC_URI[md5sum] = "a9b0816b95544d8fb1db30fce000a8e8"
SRC_URI[sha256sum] = "c57e02967b0a8e080bd9849c3c4c639846c1988e8255f22beba3d94209b0a323"

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface"
PREFERRED_VERSION_python-pyyaml = "3.10"
FILES_${PN} += " \
    /usr/share/workbench/gFEXTest.py \
    /usr/share/workbench/xadc.yml \
"
