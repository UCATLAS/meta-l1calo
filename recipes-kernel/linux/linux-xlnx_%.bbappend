FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:gfex = " \
  file://disable-cpu-idle.cfg \
"

SRC_URI:append:zynqmp = " \
  file://add-trace-for-gator.cfg \
"
