SUMMARY = "Numerical data analysis framework (OO)"
DESCRIPTION = "Object oriented framework for large scale data analysis"
HOMEPAGE = "http://root.cern.ch"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=932cfc70c6596f332fc0aaba8cdbccc0"
SRC_URI = "https://root.cern.ch/download/root_v${PV}.source.tar.gz"

S = "${WORKDIR}/${BPN}-${PV}"

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
