FILESEXTRAPATHS_prepend := "${THISDIR}/boards:"

# device tree sources for the various machines
SRC_URI_append_gfex-prototype1b = " \
    gfex/prototype1b/skeleton.dtsi  \
    gfex/prototype1b/pl.dtsi        \
    gfex/prototype1b/zynq-7000.dtsi \
    gfex/prototype1b/system.dts     \
"

SRC_URI_append_gfex-prototype2  = " \
    gfex/prototype2/skeleton.dtsi   \
    gfex/prototype2/pl.dtsi         \
    gfex/prototype2/zynq-7000.dtsi  \
    gfex/prototype2/system.dts      \
"

SRC_URI_append_gfex-prototype3  = " \
    gfex/prototype3/pcw.dtsi            \
    gfex/prototype3/pl.dtsi             \
    gfex/prototype3/system-top.dts      \
    gfex/prototype3/system.dts          \
    gfex/prototype3/zynqmp-clk-ccf.dtsi \
    gfex/prototype3/zynqmp-clk.dtsi     \
    gfex/prototype3/zynqmp.dtsi         \
"
