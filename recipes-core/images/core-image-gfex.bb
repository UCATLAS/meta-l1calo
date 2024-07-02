inherit core-image extrausers

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_INSTALL:append = " packagegroup-core-ssh-openssh"
IMAGE_INSTALL:append = " devmem2"
IMAGE_INSTALL:append = " git"
IMAGE_INSTALL:append = " zile"
IMAGE_INSTALL:append = " chrony"

IMAGE_INSTALL:append = " python3"
IMAGE_INSTALL:append = " python3-ironman"
IMAGE_INSTALL:append = " python3-numpy"
IMAGE_INSTALL:append = " python3-pandas"
# IMAGE_INSTALL:append = " python3-bigtree"
# IMAGE_INSTALL:append = " python3-humanreadable"


#IMAGE_INSTALL:append = " gcc"
IMAGE_INSTALL:append = " rsync"
IMAGE_INSTALL:append = " htop"
IMAGE_INSTALL:append = " openssh"
IMAGE_INSTALL:append = " openssh-sshd"
IMAGE_INSTALL:append = " openssh-sftp"
# IMAGE_INSTALL:append = " packagegroup-core-ssh-openssh-sftp-server"

IMAGE_INSTALL:append:gfex-production = " init-clock"
IMAGE_INSTALL:append:gfex-production = " init-ipmc-auto-shutdown"
IMAGE_INSTALL:append:gfex-production = " init-i2c-poll"
IMAGE_INSTALL:append:gfex-production = " init-ironman"
IMAGE_INSTALL:append:gfex-production = " init-opc-server"
IMAGE_INSTALL:append:gfex-production = " init-log-manager"
IMAGE_INSTALL:append:gfex-production = " init-resize-rootfs"
IMAGE_INSTALL:append:gfex-production = " init-load-firmware"

#IMAGE_INSTALL:append:zynqmp = " gator glew"

#EXTRA_USERS_PARAMS_zcu102-zynqmp = "usermod -P gFEX-prc011 root;"
