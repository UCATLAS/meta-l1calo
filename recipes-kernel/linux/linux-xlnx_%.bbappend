FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append_gfex += " \
  file://disable-cpu-idle.cfg \
"

SRC_URI:append_zynqmp += " \
  file://add-trace-for-gator.cfg \
"
