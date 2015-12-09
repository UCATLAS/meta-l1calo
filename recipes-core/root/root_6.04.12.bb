# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)
#
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# NOTE: multiple licenses have been detected; if that is correct you should separate
# these in the LICENSE value using & if the multiple licenses all apply, or | if there
# is a choice between the multiple licenses. If in doubt, check the accompanying
# documentation to determine which situation is applicable.
LICENSE = "Unknown LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=932cfc70c6596f332fc0aaba8cdbccc0"
SRC_URI = "https://root.cern.ch/download/root_v${PV}.source.tar.gz"
SRC_URI[md5sum] = "bb43d3c01d97cd2714e841630148c371"
SRC_URI[sha256sum] = "e02cc3297deaf5a5623ab15fbefdd992413cd41c37e39a6a5cbc9a5d0c885132"

inherit cmake

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = "\
	-Drootfit=ON \
	-Dminuit2=ON \
	-Dpython=ON \
	-Dssl=ON \
	-Dxrootd=ON \
	-Dbuiltin_freetype=ON \
"
