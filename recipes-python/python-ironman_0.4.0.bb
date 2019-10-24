inherit setuptools
require python-ironman.inc

SRC_URI[md5sum] = "e6ce2f4039b974a4eb4867b13f9d7322"
SRC_URI[sha256sum] = "dce56be0b15c9e86a73decccca2b4cb9b0f114952091123d77492fe1dd0e996c"

RDEPENDS_${PN} += "python-core python-twisted-core python-construct python-pyyaml python-zopeinterface python-netserver python-importlib"
PREFERRED_VERSION_python-pyyaml = "3.10"
FILES_${PN} += " \
    /usr/share/workbench/gFEXTest.py \
    /usr/share/workbench/xadc.yml \
"
