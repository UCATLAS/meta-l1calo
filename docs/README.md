<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Documentation for meta-l1calo](#documentation-for-meta-l1calo)
  - [Useful References and Links](#useful-references-and-links)
  - [Executive Summary](#executive-summary)
    - [Building and Deploying an OS](#building-and-deploying-an-os)
    - [General FSBL Procedure for](#general-fsbl-procedure-for)
  - [Frequently Asked Questions](#frequently-asked-questions)
    - [The auto-generated DTS from Xilinx does not have any details about the PHY chip. Is that intentional?](#the-auto-generated-dts-from-xilinx-does-not-have-any-details-about-the-phy-chip-is-that-intentional)
    - [Why can't I nest the MDIO node when defining a PHY chip for ethernet?](#why-cant-i-nest-the-mdio-node-when-defining-a-phy-chip-for-ethernet)
    - [Does macb handle the enet-reset?](#does-macb-handle-the-enet-reset)
    - [Why does the ethernet scan and only find one PHY device at 0x07 and not 0x17?](#why-does-the-ethernet-scan-and-only-find-one-phy-device-at-0x07-and-not-0x17)
    - [How can I further test my ethernet PHY?](#how-can-i-further-test-my-ethernet-phy)
    - [Why does Xilinx use 'ps7_ethernet_0' for the eval board when we use 'gem0'?](#why-does-xilinx-use-ps7_ethernet_0-for-the-eval-board-when-we-use-gem0)
    - [How can I configure the kernel options with bitbake?](#how-can-i-configure-the-kernel-options-with-bitbake)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Documentation for meta-l1calo

This documentation serves to provide a high-level overview of how to use this layer (and to an extent, any layer in OpenEmbedded) to build a custom operating system (OS). A set of PDF slides made, based on this, can be found [here](https://www.dropbox.com/s/4myn1symfnw0zi7/20180117_UnderstandingLayers.pdf?dl=0).

## Useful References and Links

- https://bootlin.com/doc/training/yocto/yocto-slides.pdf
- https://www.yoctoproject.org/
- http://git.yoctoproject.org/cgit/
- http://fpga.org/2013/05/28/how-to-design-and-access-a-memory-mapped-device-part-one/
- http://ece.gmu.edu/coursewebpages/ECE/ECE699_SW_HW/S16/viewgraphs/ECE699_lecture_7.pdf
- http://fpga.org/2013/05/28/how-to-design-and-access-a-memory-mapped-device-part-two/

## Executive Summary

### Building and Deploying an OS

**References**:
  - [Building and Deploying an OS](Building-and-Deploying-an-OS.md)

1. Find an available machine with various requirements for setting up
2. Get the yocto project by cloning the repository and initialize environment
3. Download and set up the additional layer dependencies, including [meta-l1calo](https://github.com/kratsg/meta-l1calo) and [meta-xilinx](https://github.com/Xilinx/meta-xilinx).
4. Configure the build system by editing `local.conf` to take advantage of parallel make and setting the machine (target architecture).
5. Build the default base image by running `bitbake zynq-base`.
6. Use the `*.wic` file to flash the SD card [if building for a gFEX board].

### General FSBL Procedure for Zynq machines

**References**:
  - [Zynq 7: Prepare and Boot Hardware](Zynq-7-Prepare-and-Boot-Hardware.md)
  - [ZynqMP: Prepare and Boot Hardware](ZynqMP-Prepare-and-Boot-Hardware.md)

1. Generate your device tree (if not using the evaluation board) from **Xilinx SDK**
2. Generate your FSBL using **Xilinx SDK**
3. Build your kernel image, u-boot, filesystem, and cross-compiled executables using **bitbake**
4. Use Vivado to create a boot image using the generated files from **bitbake** and the generated FSBL
5. Flash the SD card with the necessary files
6. Configure the board for SD boot and Boot the board

## Frequently Asked Questions

The point of this section is to serve as a series of questions and answers that have come up during the process of putting together this repository. Hopefully, I try to be accurate and correct here.

### The auto-generated DTS from Xilinx does not have any details about the PHY chip. Is that intentional?

It is quite intentional. Xilinx SDK can only manages things that are on the chip, which the ethernet phy is not. Anything that is off chip but needs to be in the dts has to be added afterwards or via a dtsi setup. See [this answer page](http://www.xilinx.com/support/answers/61117.html) for more information.


### Why can't I nest the MDIO node when defining a PHY chip for ethernet?

macb thinks the `mdio` node is the PHY instead of the `PHY` node inside the `mdio` node. If you remove the nesting, this should allow macb to probe the PHY correctly. This is related to the following posting to the yoctoproject mailing list: https://lists.yoctoproject.org/pipermail/meta-xilinx/2015-April/000959.html .

That is how you should set up the PHY in your device tree, however, to make it work for both upstream `macb` driver as well as the linux-xlnx `emacps` driver.  This is something that only `macb` seems to do, its not the standard way
of handling phy's with device-trees. There is a planned patch so that macb can use the phy-handle property.

For reference you can find the binding documentation for macb in the kernel http://lxr.free-electrons.com/source/Documentation/devicetree/bindings/net/macb.txt, however some times you will have to look at the code to understand what some drivers use for properties/etcetera (http://lxr.free-electrons.com/source/drivers/net/ethernet/cadence/macb.c).

### Does macb handle the enet-reset?

No, macb does not handle an ethernet reset like

```
enet-reset = <&gpio0 51 0>;
```

If you need to use that GPIO pin to force a hard reset in order for your PHY to work, you may have to look into doing that using some other kernel/u-boot/etcetera feature.


### Why does the ethernet scan and only find one PHY device at 0x07 and not 0x17?

*This is related to the gFEX board but helps understand the concept.* On the gFEX board, we have two marvell PHY chips, one located at `0x07` (associated with GEM1 and not enabled) and one located at `0x17` (associated with GEM0 and is enabled). However, the ethernet scans and connects to the first PHY chip it can find - which is often not what we want to do. If this isn't the case, it is most likely another PHY chip on the same MDIO bus causing issues.

### How can I further test my ethernet PHY?

You should install `ethtool` into your image via

```
CORE_IMAGE_EXTRA_INSTALL += "ethtool"
```

This allows you to determine the PHY's state, whether it's advertising correctly, etcetera. This even allows you to force the speed and other similar configurations if the physical link is there (e.g. if the link light is lit on for the switch).


### Why does Xilinx use 'ps7_ethernet_0' for the eval board when we use 'gem0'?

The `gem0` and `ps7_ethernet_0` are just names, the original iterations of the device-trees for Zynq used the `ps7_*` naming scheme compared to the upstream device trees in the linux kernel (e.g. `gem0`). Unfortunately, the Zynq platform has had a troubled past when it comes to device tree bindings, however since making its way into the kernel source it has become much more stable, if you want to pick one naming scheme use the kernels `gem0`/etcetera scheme.

### How can I configure the kernel options with bitbake?

Refer to [this openembedded wiki](https://www.openembedded.org/wiki/Kernel_Building) which states you can do

```bash
 bitbake -c menuconfig virtual/kernel
```
