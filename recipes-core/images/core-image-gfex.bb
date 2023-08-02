inherit core-image extrausers

DESCRIPTION = "The base image for all L1Calo embedded OS work providing python at a minimum."

IMAGE_INSTALL += "packagegroup-core-ssh-openssh"
IMAGE_INSTALL += "devmem2"
IMAGE_INSTALL += "git"
IMAGE_INSTALL += "zile"
IMAGE_INSTALL += "chrony"

IMAGE_INSTALL += "python3"
IMAGE_INSTALL += "python3-ironman"
IMAGE_INSTALL += "python3-numpy"
IMAGE_INSTALL += "python3-pandas"
IMAGE_INSTALL += "python3-bigtree"
IMAGE_INSTALL += "python3-humanreadable"


#IMAGE_INSTALL += "gcc"
IMAGE_INSTALL += "rsync"
IMAGE_INSTALL += "htop"
IMAGE_INSTALL += "openssh"
IMAGE_INSTALL += "openssh-sshd"
IMAGE_INSTALL += "openssh-sftp"
IMAGE_INSTALL += "openssh-sftp-server"

IMAGE_INSTALL_append_gfex-production += "init-clock"
IMAGE_INSTALL_append_gfex-production += "init-ipmc-auto-shutdown"
IMAGE_INSTALL_append_gfex-production += "init-i2c-poll"
IMAGE_INSTALL_append_gfex-production += "init-ironman"
IMAGE_INSTALL_append_gfex-production += "init-opc-server"
IMAGE_INSTALL_append_gfex-production += "init-log-manager"
IMAGE_INSTALL_append_gfex-production += "init-resize-rootfs"
IMAGE_INSTALL_append_gfex-production += "init-load-firmware"

#IMAGE_INSTALL_append_zynqmp += "gator glew"

EXTRA_USERS_PARAMS_zcu102-zynqmp = "usermod -P gFEX-prc011 root;"
