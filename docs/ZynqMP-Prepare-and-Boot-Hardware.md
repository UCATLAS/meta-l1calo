<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [FSBL Method](#fsbl-method)
  - [Files Required](#files-required)
    - [Bitstream and Device Tree](#bitstream-and-device-tree)
    - [Filesystem](#filesystem)
    - [ARM Trusted Firmware](#arm-trusted-firmware)
    - [Exception Levels](#exception-levels)
  - [bitbake execution](#bitbake-execution)
  - [Xilinx SDK Generations](#xilinx-sdk-generations)
    - [Create the FSBL](#create-the-fsbl)
    - [Create the PMUFW](#create-the-pmufw)
    - [Create the BOOT image](#create-the-boot-image)
  - [Preparing the bootscript](#preparing-the-bootscript)
  - [Preparing the SD Card](#preparing-the-sd-card)
    - [Identify SD Card](#identify-sd-card)
    - [Erase the first few bytes](#erase-the-first-few-bytes)
    - [Configure the sectors, heads, and cylinders](#configure-the-sectors-heads-and-cylinders)
      - [Calculate new_cylinders](#calculate-new_cylinders)
    - [Create actual partitions](#create-actual-partitions)
    - [Creating filesystems on new partitions](#creating-filesystems-on-new-partitions)
    - [Copying Files Over](#copying-files-over)
  - [Booting the Zynq MPSoC Ultrascale+](#booting-the-zynq-mpsoc-ultrascale)
- [Useful Links](#useful-links)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

There are two main ways that you can boot the board using an SD Card. The way that I've been able to get working for the Zynq Ultrascale+ MPSoc "Zynq+" is via the Xilinx SDK bootgen and First Stage Boot loader (FSBL). Another possibility is through the Secondary Program Loader (SPL) but this method has various problems preventing it from working right now. This guide will focus only on the FSBL method and the general steps needed to get this working. All of this was tested on 2020.2.

# FSBL Method

The general steps are highlighted below:

1. Build your kernel image, u-boot boot script (boot.scr), u-boot binary, bootimage(boot.bin), and device tree using [bitbake](https://en.wikipedia.org/wiki/BitBake) with the [Xilinx-based Yocto build environment](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18841862/Install+and+Build+with+Xilinx+Yocto)
2. Prepare your SD card [ref](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842385/How+to+format+SD+card+for+SD+boot)
3. Copy/flash all necessary files to an SD card with a FAT32 BOOT partition and an ext4 ROOT partition.

## Files Required

The following files are required in order to get everything working.

| Originating | File Name                 | Partition | Description                                                           |
|-------------|---------------------------|-----------|------------------------------------------------------------------------
  bitbake     | boot.bin                  | BOOT (1)  | Bootgen image containing the bitstream and binaries: FSBL, U-Boot, ATF, PMUFW
  bitbake     | system.dtb                | BOOT (1)  | Device tree binary blob used by Linux, loaded into memory by U-Boot
  bitbake     | Image                     | BOOT (1)  | Linux kernel image, loaded into memory by U-Boot
  bitbake     | boot.scr                  | BOOT (1)  | User defined file that loads the kernel and rootfs
  bitbake     | u-boot.bin                | BOOT (1)  | Binary compiled u-boot bootloader
  bitbake     | zcu102-zynqmp.rootfs.ext4 | ROOT (2)  | Ramdisk image used by Linux, loaded into memory by U-Boot (rootfilesystem or rootfs)


### Bitstream and Device Tree

If you use the default compiled device tree provided by `meta-xilinx` using the `zcu102-zynqmp` machine, this will be compatible with a block design containing only the Zynq MPSoC processor IPCore (with the PS-PL clocks disabled). Otherwise, you will need to create your own machine corresponding to a custom XSA file, or use one of the already existing `gfex-production` machines from `meta-l1calo` with their associated XSA files. 

For these `gfex-production` machines in the Xilinx-based build system we are taking advantage of the ability to build a custom device tree and BOOT.BIN with bitbake using the `extra-hdf` recipe from Xilinx. This allows us to include a full XSA file describing the Zynq FPGA firmware configuation in the OS build. For the gFEX machines this XSA file is generated from the [gFEX firmware builds](https://gitlab.cern.ch/atlas-l1calo/gfex/firmware/) which are run usng the gitlab CI configuration. The output files from the build are located [on eos](https://cernbox.cern.ch/index.php/s/43JT9RHyz79gByE) with the XSA at the specific path of `devel/<commit-version-git-hash>/vitis/config#/zfpg/xsa`. These XSA files are included in the `external-hdf` [recipe files](https://github.com/kratsg/meta-l1calo/tree/master/recipes-bsp/external-hdf/files) in the meta-l1calo layer.

### Filesystem

There's a couple of ways to load the filesystem depending on which you use, such as CPIO extraction (treating it as a ramdisk), untarring into the second partition of the SD card, or flashing directly the second partition of the SD card with an ext4 image. For the purposes of the tutorial, I'll focus on flashing an ext4 image, so you would need the files with .rootfs.ext4

### Exception Levels

I haven't looked into this in detail. This will need to be added manually to avoid system crashing after the FSBL handoff, see https://forums.xilinx.com/t5/Embedded-Linux/2016-3-ZynqMP-zcu102-Wrong-exception-level-in-ATF-BL31-after/td-p/730428 for an example.

## bitbake execution

This step should be done with one or two commands:

```
bitbake <image-name>
```

An example image you might want to try is `zynq-base`. All your files will be found in `tmp/deploy/images/<machine-name>/`. This completes the first step. See [[Building-and-Deploying-an-OS]] for more details.

## Preparing the SD Card


You need to prepare the SD card with two partitions: a FAT32 BOOT partition and an ext4 ROOT partition. These partition need to contain the [files required](#files-required). The next step will explain how to copy these over. Referring to the [Xilinx wiki](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842385/How+to+format+SD+card+for+SD+boot) on preparing the SD card correctly with partitions, I will re-iterate the steps here in case the wiki changes in the future.

Note: these steps are strongly recommended to be performed on a Linux machine with `fdisk`, `dmesg`, and `partprobe` along with administrator privileges.

### Identify SD Card

Plug in the SD card into the Linux machine and identify which device file maps to it, via `dmesg | tail` which will show something like

```
[76893.874830] sd 6:0:0:2: [sde] 30318592 512-byte logical blocks: (15.5 GB/14.4 GiB)
[76893.879866]  sde: sde1 sde2
```

This is telling you that your SD card is identified by `sde` which means `/dev/sde` is the device file mapping to the SD card. For the purposes of this tutorial, I will refer to it as `/dev/sdX` but please replace it with your version where necessary.

### Create actual partitions

Let's use `fdisk` to enter and partition the card

```bash
fdisk /dev/sdX
```

If you type `p`, you should not see any partitions on the SD card. If there are partitions type `d` to delete each one.

Now the actual partitions can be created. For the first partition (which will be our BOOT), I suggest a size of 1GB to ensure enough space.

```bash
Command (m for help): n
Partition type:
 p primary (0 primary, 0 extended, 4 free)
 e extended
Select (default p): p
Partition number (1-4, default 1): 1
First sector (2048-15759359, default 2048):
Using default value 2048
Last sector, +sectors or +size{K,M,G} (2048-15759359, default 15759359): +1GB
```

and now do the same procedure for the second partition (which will hold our ROOT filesystem)

```bash
Command (m for help): n
Partition type:
 p primary (1 primary, 0 extended, 3 free)
 e extended
Select (default p): p
Partition number (1-4, default 2): 2
First sector (411648-15759359, default 411648):
Using default value 411648
Last sector, +sectors or +size{K,M,G} (411648-15759359, default 15759359):
Using default value 15759359
```

The next thing we need to do is set the bootable flag

```bash
Command (m for help): a
Partition number (1-4): 1
```

and then the partition types

```bash
Command (m for help): t
Partition number (1-4): 1
Hex code (type L to list codes): c
Changed system type of partition 1 to c (W95 FAT32 (LBA))
Command (m for help): t
Partition number (1-4): 2
Hex code (type L to list codes): 83
```

and finally, we can check the new partition table to make sure the changes look right

```bash
Command (m for help): p

Disk /dev/sdb: 8068 MB, 8068792320 bytes
249 heads, 62 sectors/track, 1020 cylinders, total 15759360 sectors
Units = sectors of 1 * 512 = 512 bytes
Sector size (logical/physical): 512 bytes / 512 bytes
I/O size (minimum/optimal): 512 bytes / 512 bytes
Disk identifier: 0x920c958b

 Device Boot Start End Blocks Id System
/dev/sdb1 * 2048 411647 204800 c W95 FAT32 (LBA)
/dev/sdb2 411648 15759359 7673856 83 Linux
 ```

and then write the partition table to disk

```bash
Command (m for help): w
The partition table has been altered!

Calling ioctl() to re-read partition table.

WARNING: If you have created or modified any DOS 6.x
partitions, please see the fdisk manual page for additional
information.
Syncing disks.
```

which should hopefully go off without a hitch. If there's a syncing problem, try

```bash
partprobe /dev/sdX
```

and if that still doesn't work, you might try [turning it off and on again](https://www.youtube.com/watch?v=rksCTVFtjM4) and seeing if the kernel syncs the changes. If it still doesn't, try a different SD card.

### Creating filesystems on new partitions

Next, is the easiest of all the steps here. Our BOOT partition will be a FAT32 filesystem (makes it easy to copy files onto it from Windows/Mac/Linux) and our ROOT will be EXT4.

```bash
mkfs.vfat -F 32 -n BOOT /dev/sdX1
mkfs.ext4 -L ROOT /dev/sdX2
```

Note that the numbers at the end refer to the first or second partition. If you're unsure of what these are, run `fdisk -l /dev/sdX` to double-check.

### Copying Files Over

First, you should go ahead and mount the BOOT partition and copy over the necessary files normally. You may need to use sudo for the following commands.

```bash
mkdir -p /mnt/boot
mount /dev/sdX1 /mnt/boot
cp boot.bin /mnt/boot
cp boot.scr /mnt/boot
cp system.dtb /mnt/boot
cp Image /mnt/boot
cp u-boot.bin /mnt/boot
umount /dev/sdX1
```

For the `ROOT` partition, you don't need to mount it, just use dd to flash the ext4 rootfs to the partition.

```bash
dd if=rootfs.ext4 of=/dev/sdc2
```

## Booting the Zynq MPSoC Ultrascale+

Configure SW16(?) to `0xE` (according to [this forum post](https://forums.xilinx.com/t5/Xilinx-Boards-and-Kits/ZCU102-fail-to-boot-from-SD-card/m-p/739836#M14443) if you have a rev1.0 board with ES2 chip)

[![zynqmp_sw16_configuration](https://www.starwaredesign.com/images/IMG_2053-1.JPG)](https://www.starwaredesign.com/index.php/articles-and-talks/87-build-and-deploy-yocto-linux-on-the-xilinx-zynq-ultrascale-mpsoc-zcu102)

and then plug in the SD card and you should see the board boot up successfully. Refer to [this Xilinx wiki](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842446/Setup+a+Serial+Console) on setting up a serial console if you don't know how to do that.