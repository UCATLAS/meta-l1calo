require conf/machine/include/tune-zynqmp.inc
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
WKS_FILES = "sdimage-bootpart.wks"

# disable SPL
SPL_BINARY = ""

# PMU instance args
PMU_ROM ?= "${DEPLOY_DIR_IMAGE}/pmu-rom.elf"
QB_PMU_OPT = " \
    -M microblaze-fdt \
    -display none \
    -hw-dtb ${DEPLOY_DIR_IMAGE}/qemu-hw-devicetrees/multiarch/zynqmp-pmu.dtb \
    -kernel ${PMU_ROM} \
    -device loader,file=${DEPLOY_DIR_IMAGE}/pmu-${MACHINE}.elf \
    -device loader,addr=0xfd1a0074,data=0x1011003,data-len=4 \
    -device loader,addr=0xfd1a007C,data=0x1010f03,data-len=4 \
    "
