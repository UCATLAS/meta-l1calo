DEPENDS_append_gfex-prototype1b = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype1b = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype2  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype2  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

DEPENDS_append_gfex-prototype3  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype3  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= "

do_patch_append() {
  cp ${THISDIR}/files/${MACHINE}/defconfig ${S}/configs/${MACHINE}_defconfig
  cp ${WORKDIR}/files/${MACHINE}/machine.h ${S}/include/configs/${MACHINE}.h
}
