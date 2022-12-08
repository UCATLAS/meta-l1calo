<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Yocto](#yocto)
  - [Getting Yocto](#getting-yocto)
- [Open-Embedded Layers](#open-embedded-layers)
  - [Getting them](#getting-them)
- [Building a Kernel Image](#building-a-kernel-image)
  - [Configuring the Build System](#configuring-the-build-system)
  - [Setting the Machine](#setting-the-machine)
    - [What machines actually exist?](#what-machines-actually-exist)
  - [Converting cpio.gz to a u-boot uramdisk.image.gz](#converting-cpiogz-to-a-u-boot-uramdiskimagegz)
  - [Flashing image using wic](#flashing-image-using-wic)
- [Quick Build an OS!](#quick-build-an-os)
- [Extra Useful Commands](#extra-useful-commands-and-paths)
- [References](#references)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Introduction

In Giordon's words "So you want to build an OS huh? Shit's hard". But to be honest, I've managed to continue building and refining the custom operating system that he developed to fit our gFEX needs without getting too buried in the low level code, thanks to the flexibility and abstraction of the yocto build environment.

The easiest system to work with when building an OS (in my opinion) is Ubuntu, but any linux system should work. Recently we switched from using a totally open-source yocto build, to using a setup more specific to Xilinx. What this meant was that we used to have to use Vivado to build certain components separately from the OS build (mainly the boot.bin which depends strongly on the specfic hardware you're using), but now we can do one build of the OS to get all the files we need. This ease of operation did sacrifice some level of open-source, but since we're ultimately relying on Xilinx anyways, it didn't seem like too big of a price to pay.

Xilinx documents the [Yocto building procedure here](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18841883/Yocto), and we'll go through it in a bit more detail below. 

# Yocto

The first step is to get Yocto [yoctoproject](https://www.yoctoproject.org) set up. Yocto is built on top of [open-embedded](http://www.openembedded.org/)(OE) which is an entire open-source community dedicated to embedded OS solutions. Xilinx also has a whole system dedicated to OS building called petalinux. We have thus far chosen to build our OS with Yocto, but using petalinux is a perfectly valid option as well. Yocto allows us to use a very minimal lightweight version of the standard Linux kernel that allows us to customize what we want (OE) in an easy (Yocto) manner.

Since we are using Xilin hardware, we will need their software and code to work with the Zynq+ System on Chip. We use a Xilinx fork of the open-embedded ecosystem, which gives us the best of both worlds: the majority of recipes open-source as desired while still being compatible with the Xilinx-specific code. This allows us to produce all the files we need to actually boot our hardware from the OS build. 

## Getting Yocto

Most of the instructions here come from [the documentation](https://docs.yoctoproject.org/) at Yocto. Have a read through that for a more verbose explanation of the various options (or if using something other than ubuntu)


1. First, one must install the necessary dependencies for running bitbake on your ubuntu host machine
    ```
    sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev pylint3 xterm python3-subunit mesa-common-dev zstd liblz4-tool
    ```

2. OR it's also possible to use a docker image, but this was fairly developmentaly when I started a few years ago. It's probably in better shape now, but you have been warned. [[ref]](https://github.com/crops/poky-container/blob/master/README.md) (**WIP**: not fully supported, development/bleeding-edge. You have been warned.)
    ```
    # on your machine
    mkdir ~/yocto_shared
    docker run --rm -it -v ~/shared-yocto:/workdir crops/poky --workdir=/workdir
    ```

    and work inside this directory.

# OpenEmbedded Layers and Recipes 

The way the entire open-embedded infrastructure works is using layers. I'm sure one can Google an explanation for it, but the idea is really package dependency resolution and management. Essentially, everything outside the core of what is required for a function linux OS is optional. Different packages can be including by writing a recipe to get the code and build it. These recipes are packaged into different layers, and a huge number of layers and recipes already exist. This makes modifying your OS image extremely easy because a lot of the time all you have to do is include a new recipe into the definition of a specific OS image. 

You can go [here to see a list of layers](http://layers.openembedded.org/layerindex/branch/master/layers/) that is supported by the open-embedded community. You can also use [the index here](http://layers.openembedded.org/layerindex/branch/master/recipes/) to search for already existing recipes to include specific packages in your build. 

# Xilinx Yocto Manifests

Xilinx provides instructions for building a Xilinx-based Yocto OS [here on their confluence documentation](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18841862/Install+and+Build+with+Xilinx+Yocto). They use a repository management tool called Repo that uses a manifest file to manage a system that requires a number of different repositories that must function together, as in the yocto build environment where each layer is it's own repository. Xilinx provides the manifest necessary at [Xilinx/yocto-manifests](https://github.com/Xilinx/yocto-manifests/tree/rel-v2020.2), with a branch for each Vivado/Vitis version that one might like to use. When you use the Xilinx based setup, it will create a `./sources` directory where all the layers will be cloned, a `./build` directory where your build configuration files will live, and a setupsdk script which (shocker) sets up the Xilinx sdk or software development kit. 

## Getting Set Up

Our custom layer is called meta-l1calo. It includes a [setup script](https://github.com/kratsg/meta-l1calo/blob/master/scripts/setup.sh) which you can download and run to setup the build environment. In the folder where you run the script, it will creat a `xilinx_bitbake` folder and then install repo, initalize it to xilinx yocto-manifests, checkout the current branch used (2020.2 as of September 2022), and clone our custom meta-l1calo layer and add it to the layers used by the build engine bitbake. The Xilinx build references a high number of layers, not all of which are actually used in our build, but all of which are downloaded and included in the build for ease of use with the Xilinx system. Our custom layer `meta-l1calo` is generally used in development mode, and not necessarily frozen to a release, though it will be compatible with specific versions of the xilinx openembedded layers.

After running this setup script you can look at your bitbake layers configuration file which can be found at `xilinx_bitbake/build/conf/bblayers.conf` assuming you used the meta-l1calo setup script. One can also manually edit the `conf/bblayers.conf` file to add layers in or use the command `bitbake-layers add-layer <path-to-layer-repository>`.

My `bblayers.conf` file ended up looking like

```
LCONF_VERSION = "7"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \
  /local/home/easmith5/xilinx_bitbake/sources/core/meta \
  /local/home/easmith5/xilinx_bitbake/sources/core/meta-poky \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-perl \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-python \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-filesystems \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-gnome \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-multimedia \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-networking \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-webserver \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-xfce \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-initramfs \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-oe \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-clang \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-browser \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-qt5 \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx/meta-xilinx-bsp \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx/meta-xilinx-pynq \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx/meta-xilinx-contrib \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx-tools \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-petalinux \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-virtualization \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openamp \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-jupyter \
  /local/home/easmith5/xilinx_bitbake/sources/core/../meta-vitis-ai \
  /local/home/easmith5/xilinx_bitbake/sources/meta-l1calo \
  "

BBLAYERS_NON_REMOVABLE ?= " \
    /local/home/easmith5/xilinx_bitbake/sources/core/meta \
"
```

Either way, to make sure things looked right, I ran `bitbake-layers show-layers` which gave me the following output:

```
layer                 path                                      priority
==========================================================================
meta                  /local/home/easmith5/xilinx_bitbake/sources/core/meta  5
meta-poky             /local/home/easmith5/xilinx_bitbake/sources/core/meta-poky  5
meta-perl             /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-perl  6
meta-python           /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-python  7
meta-filesystems      /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-filesystems  6
meta-gnome            /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-gnome  7
meta-multimedia       /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-multimedia  6
meta-networking       /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-networking  5
meta-webserver        /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-webserver  6
meta-xfce             /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-xfce  7
meta-initramfs        /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-initramfs  8
meta-oe               /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openembedded/meta-oe  6
meta-clang            /local/home/easmith5/xilinx_bitbake/sources/core/../meta-clang  7
meta-browser          /local/home/easmith5/xilinx_bitbake/sources/core/../meta-browser  7
meta-qt5              /local/home/easmith5/xilinx_bitbake/sources/core/../meta-qt5  7
meta-xilinx-bsp       /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx/meta-xilinx-bsp  5
meta-xilinx-pynq      /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx/meta-xilinx-pynq  5
meta-xilinx-contrib   /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx/meta-xilinx-contrib  5
meta-xilinx-tools     /local/home/easmith5/xilinx_bitbake/sources/core/../meta-xilinx-tools  5
meta-petalinux        /local/home/easmith5/xilinx_bitbake/sources/core/../meta-petalinux  5
meta-virtualization   /local/home/easmith5/xilinx_bitbake/sources/core/../meta-virtualization  8
meta-openamp          /local/home/easmith5/xilinx_bitbake/sources/core/../meta-openamp  5
meta-jupyter          /local/home/easmith5/xilinx_bitbake/sources/core/../meta-jupyter  7
meta-vitis-ai         /local/home/easmith5/xilinx_bitbake/sources/core/../meta-vitis-ai  5
meta-l1calo           /local/home/easmith5/xilinx_bitbake/sources/meta-l1calo  7
```

where everything is ordered by priority correctly (higher number = lower priority). We want `meta-l1calo` to depend on all the above packages and so we set it to a low priority (7). For more information about the `bitbake-layers` command, see [1](#references).

# Building a Kernel Image

Building a kernel image, when all is said and done requires two main pieces of information

- the machine which you want to build on; this defines the toolchain and steps taken to build drivers, compile the device tree, etc... defined by the vendor (eg: Xilinx)
- the image you want to build on the provided machine; this defines what you actually want in your kernel image (and filesystem image) and is usually independent of the machine itself

In all cases, the machine is locally configured for a given build and you can generate multiple images for that given machine. This section will discuss some specific configurations for selecting the machine and setting up the cores. We end the section with a special command you need to do to wrap the generated diskimage in u-boot headers so that it can be extracted correctly by u-boot later.

All configurations are done in `conf/local.conf` after you've sourced the environment as mentioned previously.

## Configuring the Build System

Most of the configurations in `local.conf` worked fine for out of the box. A parallel make system helps speed up the build by increasing the number of threads, so one can set the following variables in `conf/local.conf` if desired.

```
PARALLEL_MAKE = "-j 24"
BB_NUMBER_THREADS = "24"
```

ideally you set this to the number (24) that is double the number of cores you have on the computer. If you use the setup script included in the layer as referenced above, this parallelization will be added for you, since most of the time you want to use it while building, but it should be modified for your machine. 

## Setting the Machine

In your `local.conf` file, there is a variable called `MACHINE` which specifies the hardware board target machine. There's a bunch of comments, until you get to a point where you set the machine (around line ~25 for me), so you write something like

```
MACHINE = "zcu102-zynqmp"
```

and we're done. Running `bitbake` at this point will pick up the machine you've defined and run with it.

Note: `zcu102-zynqmp` is a machine defined in `meta-xilinx` for the evaluation board of the same name. A few common machines are:

- `zc702-zynq7`
- `zc706-zynq7`
- `zcu102-zynqmp`
- `gfex-prototype1b`
- `gfex-prototype2`
- `gfex-prototype3a`
- `gfex-prototype3b`
- `gfex-prototype4`
- `gfex-production-stf`
- `gfex-production-p1`

### What machines actually exist?

The way `bitbake` works is by looking in `<layer>/conf/machine/<machine-name>.conf` to find your machine. That machine will also be tied to device-tree files which are in `<layer>/recipe-bsp/device-tree/files/<machine-name>`, usually a directory of the same name (by habit, to make everyone's life easier). At the moment the device tree for the older gfex machines (non-production boards) is not up to date with the current build system, so these may still need to be updated. 

For `meta-l1calo`, you simply look in `meta-l1calo/conf/machine` to see a list of available machines defined. Usually, you will need to define a new machine for a given device-tree specification. You can also define new machines for other reasons if preferred like our stf vs p1 gfex production machines, which use the same device tree. 

The user (aka you!) will need to think about the machine you want to use and then set that configuration yourself in the `conf/local.conf` file. (Hint: if you're working on gfex, it's probably one of the gfex-production machines). 

## Baking an Image

This is as simple as running

```
bitbake core-image-gfex
```

to build the `core-image-gfex` image for the gFEX boards. You can see what is included in this image by looking at the file in `meta-l1calo/recipes-core/images/core-image-gfex.bb`.


## Flashing image using wic

For `gfex-production` machines, the default is to create a `wic` image and an ext4 image of the rootfs. The wic image can be used to flash an entire SD card and will automatically create the necessary partitions. There are some detrimental factors to this, mainly that the wic image will be the size of the rootfs so this limits your ability to use all the space on a 60 GB SD card, for example. 

If you are using a wic image, you can flash the SD card like so

```
sudo dd if=zynq-base-gfex-prototype4.wic of=/dev/sdX
```

where `sdX` can be identified by `diskutil list` on a mac or `fdisk -l` on a Linux machine. On Windows - you can use a special tool known as [balenaEtcher](https://www.balena.io/etcher/) to do the same job.

## Manually flashing an image

Xilinx provides [documentation on how to prepare an SD card for booting](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/18842385/How+to+format+SD+card+for+SD+boot) which basically involves creating two partitions, copying files to the boot partition, and flashing the rootfs to the second partition. The instructions assume you're using an unformatted SD card and a linux PC for this. If you have existing partitions on your SD card, you can remove them by typing `d` in the fdisk interface until all partitions have been deleted. If your device had partitions that were formatted fdisk may ask at various points if you want to remove the formatting, you can say yes as you will reformat them after the partitioning is complete. 

Note: Please be careful when using fdisk! Please don't accidentally overwrite your hard drive! You can verify the name of the device you've inserted by using the command `dmesg | tail` to see the name given to the device, usually it is something like `/dev/sdX`. 

# Quick Build an OS! 

## First Time Setup
- Download the meta-l1calo [setup script](https://github.com/kratsg/meta-l1calo/blob/master/scripts/setup.sh)
- Run the setup script: ```source setup.sh```
- Configure the build with your local.conf file in ```./conf/local.conf```
  - In the file near other machine definitions set: ```MACHINE = "gfex-production-stf"```
  - Set any other needed variables 
- Build the OS: ```bitbake -c clean core-image-gfex; bitbake core-image-gfex```

## Repeatable Setup
- ```cd xilinx_bitbake``` 
- Setup Vivado SDK: ```source setupsdk```
- Configure the build with your local.conf file as needed ```.conf/local.conf```
  - In the file near other machine definitions set: ```MACHINE = "gfex-production-stf"```
  - Set any other needed variables 
- Build the OS: ```bitbake -c clean core-image-gfex; bitbake core-image-gfex```

# Extra Useful Commands and Paths

- Full kernel config is located in build directory at a path similar to the following:
  ```
  `tmp/work/<machine-name>-xilinx-linux/linux-xlnx/5.4+gitAUTOINC+62ea514294-r0/linux-<machine-name>-standard-build/.config`
  ```

- Clean a specific recipe, sometimes necessary
  ```
  bitbake -c cleanall <recipe-name>
  ```

- Clean an image, doesn't clean individual recipes
  ```
  bitbake -c clean <image-name>
  ```
  
- Converting cpio.gz to a u-boot uramdisk.image.gz
  ```
  mkimage -A arm -T ramdisk -C gzip -d inputFile.cpio.gz uramdisk.image.gz
  ```

  where `mkimage` is from `sudo apt-get install u-boot-tools`

- List recipes or images matching a pattern
  ```
  bitbake-layers show-recipes "*-image-gfex"
  ```

- Looking for an open-embedded package that includes a specific python package
  ```
  oe-pkgdata-util find-path */cgi.py

  ```

- Looking at all packages with pattern python
  ```
  oe-pkgdata-util list-packages -p python
  ```

- Show all recipes and layers they come from
  ```
  bitbake-layers show-recipes
  ```

- Show all recipes matching `python*` and the layers they come from
  ```
  bitbake-layers show-recipes "python*"
  ```
  

For more useful commands, see [2](#references).

# References

1. http://www.crashcourse.ca/wiki/index.php/BitBake_Layers
1. https://community.freescale.com/docs/DOC-94953
1. http://www.yoctoproject.org/docs/2.4.1/dev-manual/dev-manual.html#dev-manual-start
1. https://github.com/crops/poky-container/blob/master/README.md
1. https://www.yoctoproject.org/docs/1.6/bitbake-user-manual/bitbake-user-manual.html
1. http://www.yoctoproject.org/docs/2.4/mega-manual/mega-manual.html#qs-crops-build-host
