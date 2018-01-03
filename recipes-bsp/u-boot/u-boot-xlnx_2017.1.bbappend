DEPENDS_append_gfex-prototype1b = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype1b = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= " 

DEPENDS_append_gfex-prototype2  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype2  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= " 

DEPENDS_append_gfex-prototype3  = " device-tree"
EXTRA_OEMAKE_append_gfex-prototype3  = " EXT_DTB=${RECIPE_SYSROOT}/boot/devicetree/${MACHINE}.dtb dtb_depends= " 

FILESEXTRAPATHS_prepend_gfex-prototype3 := "${THISDIR}/${PN}:"
SRC_URI_append_gfex-prototype3 += "file://0001-add-gfex-prototype3.patch"
SRC_URI_append_gfex-prototype3 += "file://0002-add-gfex-prototype3-defconfig.patch"
