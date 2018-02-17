DEPENDS_append_gfex-prototype1b = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype1b = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype2  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype2  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype3  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype3  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append += "file://machine.h"
SRC_URI_append += "file://defconfig"

do_copy_configs () {
    cp ${WORKDIR}/defconfig ${S}/configs/${MACHINE}_defconfig
    cp ${WORKDIR}/machine.h ${S}/include/configs/${MACHINE}.h
}

do_patch_append() {
    bb.build.exec_func('do_copy_configs', d)
}
