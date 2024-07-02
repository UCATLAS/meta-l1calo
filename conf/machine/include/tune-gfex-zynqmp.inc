# require conf/machine/include/soc-zynqmp.inc
require conf/machine/include/tune-gfex.inc

# add overrides for group of machines
MACHINEOVERRIDES .= ":gfex-production"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-xlnx"
# PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-xlnx"
# PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-zynq-scr"
# PREFERRED_PROVIDER_virtual/pmu-firmware ?= "zynqmp-pmu-pmu-firmware"
PREFERRED_PROVIDER_virtual/pmu-firmware ?= "pmufw"


# U-Boot Configuration
PREFERRED_PROVIDER_virtual/bootloader ??= "u-boot-xlnx"
PREFERRED_PROVIDER_virtual/boot-bin ??= "xilinx-bootbin"
PREFERRED_PROVIDER_u-boot-tools ??= "u-boot-tools-xlnx"
PREFERRED_PROVIDER_u-boot-tools-native ??= "u-boot-tools-xlnx-native"
PREFERRED_PROVIDER_nativesdk-u-boot-tools ??= "nativesdk-u-boot-tools-xlnx"


#    u-boot-zynq-scr \ (from inside extra_imagedepends)
EXTRA_IMAGEDEPENDS += " \
    u-boot-xlnx \
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
