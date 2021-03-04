require conf/machine/include/soc-zynqmp.inc
require conf/machine/include/tune-gfex.inc

# add overrides for group of machines
MACHINEOVERRIDES .= ":gfex-zynqmp"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/pmu-firmware ?= "zynqmp-pmu-pmu-firmware"

EXTRA_IMAGEDEPENDS += " \
    arm-trusted-firmware \
    virtual/pmu-firmware \
    "

# image for SPI
IMAGE_FSTYPES += "jffs2"
JFFS2_ERASEBLOCK = "0x20000"

# image for SD card
IMAGE_FSTYPES += "wic"
WKS_FILES = "gfex-sdimage.wks"

# disable SPL
SPL_BINARY = ""

# PMU instance args
PMU_ROM ?= "${DEPLOY_DIR_IMAGE}/pmu-rom.elf"
