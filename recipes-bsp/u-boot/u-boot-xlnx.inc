DEPENDS_append_gfex-prototype1b = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype1b = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype2  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype2  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype3  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype3  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype4  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype4  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append_gfex-prototype1b += "file://machine.h"
SRC_URI_append_gfex-prototype1b += "file://defconfig"
SRC_URI_append_gfex-prototype2 += "file://machine.h"
SRC_URI_append_gfex-prototype2 += "file://defconfig"
SRC_URI_append_gfex-prototype3 += "file://machine.h"
SRC_URI_append_gfex-prototype3 += "file://defconfig"
SRC_URI_append_gfex-prototype4 += "file://machine.h"
SRC_URI_append_gfex-prototype4 += "file://defconfig"

do_copy_configs () {
    cp ${WORKDIR}/defconfig ${S}/configs/${MACHINE}_defconfig
    cp ${WORKDIR}/machine.h ${S}/include/configs/${MACHINE}.h
}

do_patch_append_gfex-prototype1b() {
    bb.build.exec_func('do_copy_configs', d)
}

do_patch_append_gfex-prototype2() {
    bb.build.exec_func('do_copy_configs', d)
}

do_patch_append_gfex-prototype3() {
    bb.build.exec_func('do_copy_configs', d)
}

do_patch_append_gfex-prototype4() {
    bb.build.exec_func('do_copy_configs', d)
}
