FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_gfex-prototype3 += "file://0001-no-SDCard-available-for-gFEX-prototype-v3.patch"
SRC_URI_append_gfex-prototype3 += "file://0002-add-gfex-prototype3-defconfig.patch"
