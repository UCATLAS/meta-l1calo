SUMMARY = "DS-5 Gator daemon"
DESCRIPTION = "Target-side daemon gathering data for ARM Streamline Performance Analyzer."

LICENSE = "GPL-2"
LIC_FILES_CHKSUM = "file://driver/COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCREV = "3ff46fedd4d097813156069edab9704cc65e0a42"
PV = "6.7+git${SRCPV}"

SRC_URI = "git://github.com/ARM-software/gator.git;protocol=http;branch=master \           
	   file://gator.init \
	   file://DX910-SW-99002-r7p0-00rel0.tgz \
       file://0001-disable-stripping-debug-info.patch \
"

S = "${WORKDIR}/git"

inherit update-rc.d module

#EXTRA_OEMAKE = "'CFLAGS=${CFLAGS} ${TARGET_CC_ARCH} -D_DEFAULT_SOURCE -DETCDIR=\"${sysconfdir}\" \
#    'LDFLAGS=${LDFLAGS} ${TARGET_CC_ARCH}' 'CROSS_COMPILE=${TARGET_PREFIX}' \
#    'CXXFLAGS=${CXXFLAGS} ${TARGET_CC_ARCH} -fno-rtti'"
LDFLAGS=''
INHIBIT_PACKAGE_STRIP  = "1"
INSTALL_MOD_STRIP="0"
MALI_TIMELINE_PROFILING_ENABLED = "1"
MALI_FRAMEBUFFER_DUMP_ENABLED = "1"
MALE_SW_COUNTERS_ENABLED = "1"

do_compile() {
    # The regular makefile tries to be 'smart' by hardcoding ABI assumptions, let's use the clean makefile for everything.
    cp ${S}/daemon/Makefile_aarch64 ${S}/daemon/Makefile
    oe_runmake -C daemon CROSS_COMPILE=${TARGET_PREFIX} CC='${CC}' CXX='${CXX}' 

    #Build gator.ko
    GATOR_WITH_MALI_SUPPORT=MALI_4xx
    CONFIG_GATOR_MALI_4XXMP_PATH=${WORKDIR}/DX910-SW-99002-r7p0-00rel0/driver/src/devicedrv/mali/
    oe_runmake -C ${STAGING_KERNEL_DIR} M=${S}/driver ARCH=${ARCH} modules
    #ARCH=${TARGET_ARCH} modules
}

do_install() {
    install -d ${D}${sbindir}
    install -d ${D}${INIT_D_DIR}
    install -m 0755 ${S}/daemon/gatord  ${D}${sbindir}/gatord
    install -m 0755 ${WORKDIR}/gator.init ${D}${INIT_D_DIR}/gator
    install -m 0755 ${S}/driver/gator.ko ${D}${sbindir}/gator.ko
}

FILES_${PN} = " \
  ${INIT_D_DIR}/gator \
  ${sbindir}/gatord \
  ${sbindir}/gator.ko \
"

INITSCRIPT_NAME = "gator"
INITSCRIPT_PARAMS = "defaults 66"
