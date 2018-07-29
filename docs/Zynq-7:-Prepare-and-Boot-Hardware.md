<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [FSBL Method](#fsbl-method)
  - [Files Required](#files-required)
  - [Create the FSBL](#create-the-fsbl)
  - [Create the Boot Image](#create-the-boot-image)
  - [Preparing the SD Card](#preparing-the-sd-card)
- [SPL Method](#spl-method)
- [Useful Links](#useful-links)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

There are two main ways that you can boot the board using an SD Card. The way that I've been using recently is via Xilinx SDK bootgen and FSBL (First Stage Boot Loader). Another possibility is to use SPL (Secondary Program Loader) which has a similar, different set of files necessary.

# FSBL Method

The general steps are highlighted below:

1. [Generate your device tree](http://www.wiki.xilinx.com/Build+Device+Tree+Blob) (if not using the evaluation board) from Vivado
1. Build your kernel image (also provides a uImage and u-boot binary) using [bitbake](https://en.wikipedia.org/wiki/BitBake)
1. Wrap the filesystem image in u-boot headers [if necessary!]
1. Use Vivado to create a boot image from the zynq fsbl
1. Copy all necessary files to SD card (FAT32, single partition)

## Files Required

- `u-boot.elf` from [[bitbake|Building-and-Deploying-an-OS]] (recommended!) or [manually compiling the u-boot source code](http://www.wiki.xilinx.com/Build+U-Boot) (not recommended!)
- `top.bit` from your synthesized hardware project

## Create the FSBL

Open up the Xilinx SDK with the HDF (hardware description file) and BIT (bitstream file) loaded. From here, you will first create an application project for the Zynq FSBL `File > New > Application Project`:

[[images/new_application_project.png|alt=new_application_project]]

and then select the Zynq FSBL project

[[images/zynq_fsbl_wizard.png|alt=zynq_fsbl_wizard]]

and then the SDK will automatically build the necessary files, including the `fsbl.elf` file and copy over the bitstream file. The `fsbl.elf` file and the bitstream file will most likely be found in the `Debug/` folder under that project. This completes the first step.

## Create the Boot Image

Now, we just need to create the boot image `BOOT.BIN` with the necessary files loaded in the correct order. From the Xilinx SDK, `Xilinx Tools > Create Boot Image` which brings up a dialog

[[images/create_boot_image_dialog.png|alt=create_boot_image_dialog]]

where we need to have the following files loaded in exactly this order and type

| File Type             | File Name                                |
------------------------|-------------------------------------------
bootloader              | /path/to/sdk/fsbl_project/Debug/fsbl.elf |
datafile                | /path/to/sdk/fsbl_project/Debug/top.bit  |
datafile                | /path/to/bitbake/files/u-boot.elf        |

and then we can go ahead and `Create Image`. This will be created in the FSBL project under the `bootimage/` folder.

## Preparing the SD Card

The SD Card should have the following files

| Originating | File Name             | Description                                                           |
|-------------|-----------------------|------------------------------------------------------------------------
  Xilinx SDK  | BOOT.BIN              | Binary image containing the FSBL and U-Boot images produced by bootgen
  bitbake     | devicetree.dtb        | Device tree binary blob used by Linux, loaded into memory by U-Boot
  bitbake     | uramdisk.image.gz     | Ramdisk image used by Linux, loaded into memory by U-Boot
  bitbake     | u-boot.elf            | U-Boot elf file used to create the BOOT.BIN image
  bitbake     | uImage                | Linux kernel image, loaded into memory by U-Boot
  Xilinx SDK  | fsbl.elf              | FSBL elf image used to create BOOT.BIN image


# SPL Method

[This method is currently documented here](https://github.com/Xilinx/meta-xilinx/blob/master/README.booting.md#loading-via-sd). It has not been used by us yet.

# Useful Links

- http://www.wiki.xilinx.com/Prepare+Boot+Medium
- http://www.wiki.xilinx.com/Ubuntu+on+Zynq
- http://www.wiki.xilinx.com/Zynq+Release+14.4 and http://www.wiki.xilinx.com/Zynq+2015.2+Release
- http://www.wiki.xilinx.com/Zynq+Linux
- http://www.wiki.xilinx.com/U-boot
- https://github.com/Xilinx/embeddedsw/tree/master/lib/sw_apps/zynq_fsbl/src
- http://git.denx.de/?p=u-boot.git;a=tree;f=board/xilinx/zynq;h=2fc205e4e97071b73a16c00dec1d6804e418343f;hb=HEAD
- https://github.com/Xilinx/meta-xilinx/blob/master/README.booting.md#loading-via-sd
- http://www.wiki.xilinx.com/U-Boot+Secondary+Program+Loader#Task%20Description-Build%20U-Boot%20SPL
