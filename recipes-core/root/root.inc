SUMMARY = "Numerical data analysis framework (OO)"
DESCRIPTION = "Object oriented framework for large scale data analysis"
HOMEPAGE = "http://root.cern.ch"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5ec773ab82cbea1f17ec5b98e8ce60cf"
SRC_URI = "https://root.cern.ch/download/root_v${PV}.source.tar.gz"

S = "${WORKDIR}/${BPN}-${PV}"

TOOLCHAIN = "clang"
#TARGET_CXXFLAGS_append_toolchain-clang = " -stdlib=libc++ "
TUNE_CCARGS_remove_toolchain-clang = "-march=armv7-a"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains("TUNE_FEATURES", "armv7a", " -march=armv7a", "",d)}"

DEPENDS += "clang libpcre lz4 libx11 libxpm libxft gsl fftw libatomic-ops freetype glew xrootd"

inherit cmake pkgconfig pythonnative


do_configure_prepend(){
	export FC=${GFORTRAN}
}

EXTRA_OECMAKE = "\
    -Drootfit=ON \
    -Dminuit2=ON \
    -Dpython=ON \
    -Dssl=ON \
    -Dxrootd=ON \
    -Dbuiltin_freetype=OFF \
    -Dbuiltin_llvm=OFF \
    -Dbuiltin_glew=OFF \
    -Dastiff=OFF \
    -Dasimage=OFF \
    -Dbuiltin_afterimage=OFF \
    -Dalien=OFF \
    -Dvdt=OFF \
"
