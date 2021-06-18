FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_zcu102-zynqmp += " \
  file://disable-cpu-idle.cfg \
"
SRC_URI_append_zcu102-zynqmp += " \
  file://add-for-pynq.cfg \
"
KERNEL_FEATURES_append_zcu102-zynqmp = " disable-cpu-idle.cfg"
KERNEL_FEATURES_append_zcu102-zynqmp = " add-for-pynq.cfg"