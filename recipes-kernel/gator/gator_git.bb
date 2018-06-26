SUMMARY = "DS-5 Gator daemon"
DESCRIPTION = "Target-side daemon gathering data for ARM Streamline Performance Analyzer."

LICENSE = "GPL-2"
LIC_FILES_CHKSUM = "file://driver/COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCREV = "7ca6004c0b05138c49b9b21e0045487f55a60ab6"
PV = "5.22+git${SRCPV}"

SRC_URI = "git://git.linaro.org/arm/ds5/gator.git;protocol=http;branch=linaro \
           file://gator.init"

S = "${WORKDIR}/git"

inherit update-rc.d

# Since this is c++ code we need to both compile and link with CXX
#| PerfSource.o: In function `PerfSource::~PerfSource()':
#| /usr/src/debug/gator/5.22+gitAUTOINC+7ca6004c0b-r0/git/daemon/PerfSource.cpp:128: undefined reference to `operator delete(void*, unsigned long)'
CCLD = "${CXX}"

EXTRA_OEMAKE = "'CFLAGS=${CFLAGS} ${TARGET_CC_ARCH} -D_DEFAULT_SOURCE -DETCDIR=\"${sysconfdir}\"' \
    'LDFLAGS=${LDFLAGS} ${TARGET_CC_ARCH}' 'CROSS_COMPILE=${TARGET_PREFIX}' \
    'CXXFLAGS=${CXXFLAGS} ${TARGET_CC_ARCH} -fno-rtti'"

do_compile() {
    # The regular makefile tries to be 'smart' by hardcoding ABI assumptions, let's use the clean makefile for everything.
    cp ${S}/daemon/Makefile_aarch64 ${S}/daemon/Makefile
    # Allow using a differnt linker than $(CC)
    sed -i -e 's:$(CC) $(LDFLAGS):$(CCLD) $(LDFLAGS):' ${S}/daemon/common.mk
    oe_runmake -C daemon CC='${CC}' CXX='${CXX}'
}

do_install() {
    install -D -p -m0755 daemon/gatord ${D}/${sbindir}/gatord
    install -D -p -m0755 ${WORKDIR}/gator.init ${D}/${sysconfdir}/init.d/gator
}

INITSCRIPT_NAME = "gator"
INITSCRIPT_PARAMS = "defaults 66"
