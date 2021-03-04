
require conf/machine/include/machine-xilinx-default.inc
require conf/machine/include/xilinx-board.inc
include conf/machine/include/zynqmp-pmu-config.inc

# this is usually something you don't touch, but specify a list of features/tunes
MACHINE_FEATURES = "rtc ext2 ext3 vfat usbhost"
# serial connection with baud rate 115200
SERIAL_CONSOLE = "115200 ttyPS0"
# erase kernel device tree because we want to build our own
KERNEL_DEVICETREE = ""

MACHINEOVERRIDES .= ":gfex"
