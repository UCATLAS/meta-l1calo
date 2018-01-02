FILESEXTRAPATHS_prepend := "${THISDIR}/boards:"

# device tree sources for the various machines
SRC_URI_append_gfex-prototype1b = " \
    file://gfex/prototype1b/skeleton.dtsi  \
    file://gfex/prototype1b/pl.dtsi        \
    file://gfex/prototype1b/zynq-7000.dtsi \
    file://gfex/prototype1b/system.dts     \
"

SRC_URI_append_gfex-prototype2  = " \
    file://gfex/prototype2/skeleton.dtsi   \
    file://gfex/prototype2/pl.dtsi         \
    file://gfex/prototype2/zynq-7000.dtsi  \
    file://gfex/prototype2/system.dts      \
"

SRC_URI_append_gfex-prototype3  = " \
    file://gfex/prototype3/pcw.dtsi            \
    file://gfex/prototype3/pl.dtsi             \
    file://gfex/prototype3/system-top.dts      \
    file://gfex/prototype3/system.dts          \
    file://gfex/prototype3/zynqmp-clk-ccf.dtsi \
    file://gfex/prototype3/zynqmp-clk.dtsi     \
    file://gfex/prototype3/zynqmp.dtsi         \
"
