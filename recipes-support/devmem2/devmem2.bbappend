FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = "file://devmem2-fixups-2.patch;apply=yes;striplevel=0 \
                  file://0001-devmem.c-ensure-word-is-32-bit-and-add-support-for-6.patch"
