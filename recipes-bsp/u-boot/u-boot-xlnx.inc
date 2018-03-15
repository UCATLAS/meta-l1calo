# not 100% sure what these do yet but it tells device-tree.bb to actually build
# dtb files for the device tree sources
DEPENDS_append_gfex-prototype1b = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype1b = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype2  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype2  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype3a  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype3a  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype3b  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype3b  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype4  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype4  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

# add current directory FILES to the search path
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append_gfex-prototype1b += "file://machine.h"
SRC_URI_append_gfex-prototype1b += "file://defconfig"
SRC_URI_append_gfex-prototype2 += "file://machine.h"
SRC_URI_append_gfex-prototype2 += "file://defconfig"
SRC_URI_append_gfex-prototype3a += "file://machine.h"
SRC_URI_append_gfex-prototype3a += "file://defconfig"
SRC_URI_append_gfex-prototype3b += "file://machine.h"
SRC_URI_append_gfex-prototype3b += "file://defconfig"
SRC_URI_append_gfex-prototype4 += "file://machine.h"
SRC_URI_append_gfex-prototype4 += "file://defconfig"

# a bash function that copies the defconfig and machine.h file from the SRC_URI
# to the sysroot directory to be discoverable by u-boot
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

do_patch_append_gfex-prototype3a() {
    bb.build.exec_func('do_copy_configs', d)
}

do_patch_append_gfex-prototype3b() {
    bb.build.exec_func('do_copy_configs', d)
}

do_patch_append_gfex-prototype4() {
    bb.build.exec_func('do_copy_configs', d)
}
