# not 100% sure what these do yet but it tells device-tree.bb to actually build
# dtb files for the device tree sources
DEPENDS_append_gfex = " device-tree"
EXTRA_OEMAKE_append_gfex = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

# add current directory FILES to the search path
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append_gfex += " \
  file://machine.h \
  file://defconfig \
"

# a bash function that copies the defconfig and machine.h file from the SRC_URI
# to the sysroot directory to be discoverable by u-boot
do_copy_configs () {
    cp ${WORKDIR}/defconfig ${S}/configs/${MACHINE}_defconfig
    cp ${WORKDIR}/machine.h ${S}/include/configs/${MACHINE}.h
}

do_patch_append_gfex() {
    bb.build.exec_func('do_copy_configs', d)
}
