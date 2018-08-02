# README

This documentation serves to provide a high-level overview of how to use this layer (and to an extent, any layer in OpenEmbedded) to build a custom operating system (OS). A set of PDF slides made, based on this, can be found [here](https://www.dropbox.com/s/4myn1symfnw0zi7/20180117_UnderstandingLayers.pdf?dl=0).

## Useful References and Links

- https://bootlin.com/doc/training/yocto/yocto-slides.pdf
- https://www.yoctoproject.org/
- http://git.yoctoproject.org/cgit/

## Executive Summary

### [Building and Deploying an OS](Building-and-Deploying-an-OS.md)
1. Find an available machine with various requirements for setting up
2. Get the yocto project by cloning the repository and initialize environment
3. Download and set up the additional layer dependencies, including [meta-l1calo](https://github.com/kratsg/meta-l1calo) and [meta-xilinx](https://github.com/Xilinx/meta-xilinx).
4. Configure the build system by editing `local.conf` to take advantage of parallel make and setting the machine (target architecture).
5. Build the default base image by running `bitbake zynq-base`.
6. Use the `*.wic` file to flash the SD card [if building for a gFEX board].

### General FSBL Procedure for [Zynq 7](Zynq-7:-Prepare-and-Boot-Hardware.md) and [ZynqMP](ZynqMP:-Prepare-and-Boot-Hardware.md)
1. Generate your device tree (if not using the evaluation board) from **Xilinx SDK**
2. Generate your FSBL using **Xilinx SDK**
3. Build your kernel image, u-boot, filesystem, and cross-compiled executables using **bitbake**
4. Use Vivado to create a boot image using the generated files from **bitbake** and the generated FSBL
5. Flash the SD card with the necessary files
6. Configure the board for SD boot and Boot the board

