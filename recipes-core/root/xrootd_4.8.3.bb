SUMMARY = "%{name}"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5da7e85e91d892c5f88216fa5470012a"
SRC_URI = "https://github.com/xrootd/xrootd/archive/v${PV}.tar.gz \
           file://0001-Remove-hardcoded-usr-local-includes-from-cmake.patch \
"
SRC_URI[md5sum] = "f67294ab2a9476ef89240d8f687ae683"
SRC_URI[sha256sum] = "c722140045f1b0283b8d0bda4e56299ae4d698ae477333343fb302bf33c9d679"

inherit cmake

DEPENDS += "python"

# ENABLE_CRYPTO
#DEPENDS += "openssl"

# ENABLE_KRB5
#DEPENDS += "krb5"

EXTRA_OECMAKE = " \
  -DENABLE_PERL=FALSE \
  -DENABLE_FUSE=FALSE \
  -DENABLE_CRYPTO=FALSE \
  -DENABLE_KRB5=FALSE \
  -DENABLE_FUSE=FALSE \
  -DENABLE_READLINE=FALSE \
  -DHAVE_ATOMICS_EXITCODE=0 \
"

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
