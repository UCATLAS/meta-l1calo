FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_gfex += " \
  file://disable-cpu-idle.cfg \
  file://0001-mtd-spi-nor-Added-support-for-new-flash-parts.patch        \
  file://0002-spi-Add-support-for-runtime-idle.patch \
  file://0003-pll-shutdown-debug.patch \
  file://0004-spi-spi-xilinx-passed-correct-structure-in-pm-calls.patch \
"
