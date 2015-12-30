DESCRIPTION = "Object oriented framework for large scale data analysis"
HOMEPAGE = "http://root.cern.ch"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=932cfc70c6596f332fc0aaba8cdbccc0"
SRC_URI = "https://root.cern.ch/download/root_v${PV}.source.tar.gz"
SRC_URI[md5sum] = "bb43d3c01d97cd2714e841630148c371"
SRC_URI[sha256sum] = "e02cc3297deaf5a5623ab15fbefdd992413cd41c37e39a6a5cbc9a5d0c885132"

inherit cmake pkgconfig

DEPENDS += "llvm3.3 libx11 libxpm libxft python-core"

do_configure_prepend(){
	export FC=${GFORTRAN}
}

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = "\
	-Drootfit=ON \
	-Dminuit2=ON \
	-Dpython=ON \
	-Dssl=ON \
	-Dxrootd=ON \
	-Dbuiltin_freetype=ON \
        -Dbuiltin_llvm=OFF \
"
