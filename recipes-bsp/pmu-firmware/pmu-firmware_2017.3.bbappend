# Please enable debug logs in pmufw by defining XPFW_DEBUG_DETAILED and DEBUG_MODE macros in pmufw source code.
# See http://www.wiki.xilinx.com/PMU+Firmware for more flags.

EXTRA_COMPILER_FLAGS_append = " -DXPFW_DEBUG_DETAILED -DDEBUG_MODE"
