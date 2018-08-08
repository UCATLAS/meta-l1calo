FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_gfex += " \
  file://disable-cpu-idle.cfg \
"

SRC_URI_append_zynqmp += " \
  file://add-trace-for-gator.cfg \
"
