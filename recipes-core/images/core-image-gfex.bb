inherit core-image extrausers

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_INSTALL += "packagegroup-core-ssh-openssh"
IMAGE_INSTALL += "devmem2"
IMAGE_INSTALL += "git"

IMAGE_INSTALL += "python-ironman"
IMAGE_INSTALL += "python-periphery"

#IMAGE_INSTALL_gfex-prototype4 += "init-clock"
#IMAGE_INSTALL_append_zynqmp += "gator glew"

EXTRA_USERS_PARAMS_zcu102-zynqmp = "usermod -P gFEX-prc011 root;"