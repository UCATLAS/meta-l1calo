inherit core-image

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_INSTALL += "python-ironman"
IMAGE_INSTALL += "devmem2"
IMAGE_INSTALL += "packagegroup-core-ssh-openssh"

