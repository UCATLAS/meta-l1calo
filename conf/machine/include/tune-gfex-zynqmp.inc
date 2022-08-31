require conf/machine/include/soc-zynqmp.inc
require conf/machine/include/tune-gfex.inc

# add overrides for group of machines
MACHINEOVERRIDES .= ":gfex-production"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/pmu-firmware ?= "zynqmp-pmu-pmu-firmware"

EXTRA_IMAGEDEPENDS += " \
    u-boot-zynq-scr \
    arm-trusted-firmware \
    virtual/pmu-firmware \
    "

YAML_ENABLE_DT_OVERLAY = "1"

# image for SPI
IMAGE_FSTYPES += "jffs2 ext4 wic"
JFFS2_ERASEBLOCK = "0x20000"
WKS_FILES = "gfex-sdimage.wks"

# disable SPL
SPL_BINARY = ""

# PMU instance args
PMU_ROM ?= "${DEPLOY_DIR_IMAGE}/pmu-rom.elf"
