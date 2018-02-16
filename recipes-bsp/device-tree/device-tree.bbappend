FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# needed to populate sysroot so that u-boot can find the *.dtb
SYSROOT_DIRS += "/boot/devicetree"

# device tree sources for the various machines
#COMPATIBLE_MACHINE_gfex-prototype1b = ".*"
SRC_URI_append_gfex-prototype1b = " \
    file://skeleton.dtsi  \
    file://pl.dtsi        \
    file://zynq-7000.dtsi \
    file://system.dts     \
"

#COMPATIBLE_MACHINE_gfex-prototype2 = ".*"
SRC_URI_append_gfex-prototype2  = " \
    file://skeleton.dtsi   \
    file://pl.dtsi         \
    file://zynq-7000.dtsi  \
    file://system.dts      \
"

#COMPATIBLE_MACHINE_gfex-prototype3 = ".*"
SRC_URI_append_gfex-prototype3  = " \
    file://pcw.dtsi            \
    file://pl.dtsi             \
    file://gfex-prototype3.dts      \
    file://zynqmp-clk-ccf.dtsi \
    file://zynqmp-clk.dtsi     \
    file://zynqmp.dtsi         \
"

#COMPATIBLE_MACHINE_gfex-prototype4 = ".*"
SRC_URI_append_gfex-prototype4  = " \
    file://pcw.dtsi            \
    file://pl.dtsi             \
    file://gfex-prototype4.dts      \
    file://zynqmp-clk-ccf.dtsi \
    file://zynqmp-clk.dtsi     \
    file://zynqmp.dtsi         \
"
