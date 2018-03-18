# Have bitbake include ${THISDIR}/files to the list of search paths
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# needed to populate sysroot so that u-boot can find the *.dtb
# this is very specific to Xilinx and needs to be included somewhere
# so that the u-boot recipe can find it
SYSROOT_DIRS += "/boot/devicetree"

COMPATIBLE_MACHINE_gfex = ".*"

# device tree sources for the various machines
# these are the file paths for the device tree information
SRC_URI_append_gfex-prototype1b = " \
    file://skeleton.dtsi  \
    file://pl.dtsi        \
    file://zynq-7000.dtsi \
    file://system.dts     \
"

SRC_URI_append_gfex-prototype2  = " \
    file://skeleton.dtsi   \
    file://pl.dtsi         \
    file://zynq-7000.dtsi  \
    file://system.dts      \
"

SRC_URI_append_gfex-zynqmp = " \
    file://pl.dtsi             \
    file://zynqmp-clk-ccf.dtsi \
    file://zynqmp-clk.dtsi     \
    file://zynqmp.dtsi         \
"

SRC_URI_append_gfex-prototype3a  = " \
    file://pcw.dtsi            \
    file://gfex-prototype3a.dts      \
"

SRC_URI_append_gfex-prototype3b  = " \
    file://pcw.dtsi            \
    file://gfex-prototype3b.dts      \
"

SRC_URI_append_gfex-prototype4  = " \
    file://pcw.dtsi            \
    file://gfex-prototype4.dts      \
"
