inherit setuptools
require python-ironman.inc

SRC_URI[md5sum] = "bf773a39ebc0c349869a344ed4742a1c"
SRC_URI[sha256sum] = "d51602f840b3c843868639a2e2518f7fbaf1046dbf1057207c9253bc3fcaaee9"

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface python-netserver"
PREFERRED_VERSION_python-pyyaml = "3.10"
FILES_${PN} += " \
    /usr/share/workbench/gFEXTest.py \
    /usr/share/workbench/xadc.yml \
"
