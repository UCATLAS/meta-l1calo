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

PACKAGECONFIG ??= "crypto krb5"
PACKAGECONFIG[crypto] = "-DENABLE_KRB5=TRUE,-DENABLE_KRB5=FALSE,openssl"
PACKAGECONFIG[krb5] = "-DENABLE_CRYPTO=TRUE,-DENABLE_CRYPTO=FALSE,krb5"

EXTRA_OECMAKE = " \
  -DENABLE_PERL=FALSE \
  -DENABLE_FUSE=FALSE \
  -DENABLE_FUSE=FALSE \
  -DENABLE_READLINE=FALSE \
  -DHAVE_ATOMICS_EXITCODE=0 \
"

# xrootd is installing plugins and shared libraries to the same location
# which is not so easy to change. The best fix for now is to put all of them in /usr/lib
# and skip the QA for this. This is an acceptable workaround but we will end up with .so symlinks
# in the run-time package, which doesn't do much harm.
# See archive for discussion: https://lists.yoctoproject.org/pipermail/yocto/2018-July/041728.html .
INSANE_SKIP_${PN} += "dev-so"
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/*.so"
