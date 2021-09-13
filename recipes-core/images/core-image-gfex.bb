inherit core-image extrausers

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_INSTALL += "packagegroup-core-ssh-openssh"
IMAGE_INSTALL += "devmem2"
IMAGE_INSTALL += "git"
IMAGE_INSTALL += "zile"

IMAGE_INSTALL += "python3-ironman"
IMAGE_INSTALL += "python3-numpy"
IMAGE_INSTALL += "python3"
IMAGE_INSTALL += "gfex-register-access"

#IMAGE_INSTALL += "gcc"

IMAGE_INSTALL_append_gfex-prototype4 += "init-clock"
IMAGE_INSTALL_append_gfex-prototype4 += "init-ipmc-auto-shutdown"
IMAGE_INSTALL_append_gfex-prototype4 += "init-i2c-poll"

#IMAGE_INSTALL_append_zynqmp += "gator glew"

EXTRA_USERS_PARAMS_zcu102-zynqmp = "usermod -P gFEX-prc011 root;"