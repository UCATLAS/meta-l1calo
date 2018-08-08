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
- [Extra Useful Commands](#extra-useful-commands)
- [References](#references)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

So you want to build an OS huh? Shit's hard. You get yourself neck-deep in kernel code which is pretty terrible. The absolute requirement is that you have a linux system to work on (or at the very least a windows computer with a VirtualBox VM). I prefer using Ubuntu for this exercise, but anything should work.

# Yocto

The first step is to get ready by getting the [yoctoproject](https://www.yoctoproject.org) set up. Yocto is built on top of [open-embedded](http://www.openembedded.org/) which is an entire open-source community dedicated to embedded OS solutions. Rather than trying to do what Xilinx wants us to do with PetaLinux or something similar, we will work using the standard Linux kernel, but a very minimal, lightweight version that allows us to customize what we want (OE) in an easy (Yocto) manner.

## Getting Yocto

Most of the instructions here come from [the documentation](http://www.yoctoproject.org/docs/2.0/mega-manual/mega-manual.html) at Yocto. Have a read through that for a more verbose explanation of the various options (or if using something other than ubuntu)


1. First, we need all the necessary packages
    ```
    sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib \
       build-essential chrpath socat libsdl1.2-dev xterm
    ```

1. Or we use a docker image [[ref]](https://github.com/crops/poky-container/blob/master/README.md) (**WIP**: not fully supported, development/bleeding-edge. You have been warned.)
    ```
    # on your machine
    mkdir ~/yocto_shared
    docker run --rm -it -v ~/shared-yocto:/workdir crops/poky --workdir=/workdir
    ```

    and work inside this directory.

1. Then we get the Yocto project release and checkout a specific version (in a branch)
    ```
    git clone -b rocko git://git.yoctoproject.org/poky
    ```

1. Finally, we just need to initialize our environment
    ```
    cd poky/
    source oe-init-build-env
    ```

  and we are good to go! Note that this will set up our environment and then put us into a `~/poky/build` directory from which all builds are set up. All local configurations must be done relative to this directory!

# Open-Embedded Layers

The way the entire open-embedded infrastructure works is using layers. I'm sure one can Google an explanation for it, but the idea is really package dependency resolution and management. You can drop in specific functionality that you would want in your OS simply by checking off the packages and the layers are what contains recipes for how to add these things into your OS when it builds an image.

You can go [here to see a list of layers](http://layers.openembedded.org/layerindex/branch/master/layers/) that is supported by the open-embedded community. In particular, we are going to want

- [openembedded-core](http://layers.openembedded.org/layerindex/branch/master/layer/openembedded-core/)
- [meta-oe](http://layers.openembedded.org/layerindex/branch/master/layer/meta-oe/)
- [meta-xilinx-bsp](http://layers.openembedded.org/layerindex/branch/master/layer/meta-xilinx/)
- [meta-python](http://layers.openembedded.org/layerindex/branch/master/layer/meta-python/)
- [meta-l1calo](https://github.com/kratsg/meta-l1calo)

so read each of those to see the sorts of recipes each of them have.

## Getting them

In the same directory (eg: the home directory), we `git clone` all of the above

```
git clone -b rocko git://git.openembedded.org/openembedded-core
git clone -b rocko git://git.openembedded.org/meta-openembedded
git clone -b rocko git://git.yoctoproject.org/meta-xilinx
git clone git://github.com/kratsg/meta-l1calo
```

and these all need to be matched with our `poky` distribution (which is set at `rocko`) with `meta-l1calo` at the time in development mode, and not necessarily frozen to a release.

We can add all of these layers in. From your `~/poky` directory, run `source oe-init-build-env` if you haven't already so that your environment is set up and `bitbake` is found... then we add the layers by running

```
cd /path/to/poky
source oe-init-build-env
bitbake-layers add-layer ../../meta-xilinx/meta-xilinx-bsp
bitbake-layers add-layer ../../meta-openembedded/meta-oe
bitbake-layers add-layer ../../meta-openembedded/meta-python
bitbake-layers add-layer ../../openembedded-core/meta
bitbake-layers add-layer ../../meta-l1calo
```

which will add all of these layers into `conf/bblayers.conf` for us. Make sure you point to the folder containing the layer. One could also manually edit the `conf/bblayers.conf` file to add these layers in.

My `bblayers.conf` file ended up looking like

```
# POKY_BBLAYERS_CONF_VERSION is increased each time build/conf/bblayers.conf
# changes incompatibly
POKY_BBLAYERS_CONF_VERSION = "2"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \
  /local/d4/gstark/poky/meta \
  /local/d4/gstark/poky/meta-poky \
  /local/d4/gstark/poky/meta-yocto-bsp \
  /local/d4/gstark/openembedded-core/meta \
  /local/d4/gstark/meta-xilinx/meta-xilinx-bsp \
  /local/d4/gstark/meta-openembedded/meta-oe \
  /local/d4/gstark/meta-openembedded/meta-python \
  /local/d4/gstark/meta-l1calo \
  "
```

Either way, to make sure things looked right, I ran `bitbake-layers show-layers` which gave me the following output:

```
layer                 path                                      priority
==========================================================================
meta                  /local/d4/gstark/poky/meta                5
meta-poky             /local/d4/gstark/poky/meta-poky           5
meta-yocto-bsp        /local/d4/gstark/poky/meta-yocto-bsp      5
meta-xilinx           /local/d4/gstark/meta-xilinx/meta-xilinx-bsp   5
meta-oe               /local/d4/gstark/meta-openembedded/meta-oe  6
meta-python           /local/d4/gstark/meta-openembedded/meta-python  7
meta-l1calo           /local/d4/gstark/meta-l1calo              7
```

where everything is ordered by priority correctly (higher number = lower priority). We want `meta-l1calo` to depend on all the above packages and so we set it to a low priority (7). For more information about the `bitbake-layers` command, see [1](#references).

# Building a Kernel Image

Building a kernel image, when all is said and done requires two main pieces of information

- the machine which you want to build on; this defines the toolchain and steps taken to build drivers, compile the device tree, etc... defined by the vendor (eg: Xilinx)
- the image you want to build on the provided machine; this defines what you actually want in your kernel image (and filesystem image) and is usually independent of the machine itself

In all cases, the machine is locally configured for a given build and you can generate multiple images for that given machine. It's as simple as baking an image, making a white russian, and casually sipping it while it compiles it all for you. This section will discuss some specific configurations I did for selecting the machine and setting up the cores. We end the section with a special command you need to do to wrap the generated diskimage in u-boot headers so that it can be extracted correctly by u-boot later.

All configurations are done in `conf/local.conf` after you've sourced the environment as mentioned previously.

## Configuring the Build System

Most of the configurations in `local.conf` worked fine for me out of the box. I wanted a parallel make system though by increasing the number of threads, so I set the following variables

```
PARALLEL_MAKE = "-j 24"
BB_NUMBER_THREADS = "24"
```

where you set it to a number (24) that is double the number of cores you have on the computer. In this case, the computer I was working on had 12 cores, so I set this to 24 threads. Change it for your machine though, obviously. There are a lot of other supported filesystem types you can generate... however this one is the one that would be used to unpack, wrap u-boot headers, and then repack.

## Setting the Machine

In your `local.conf` file, there is a variable called `MACHINE` which specifies the hardware board target machine. There's a bunch of comments, until you get to a point where you set the machine, so you write something like

```
MACHINE = "zc706-zynq7"
```

and we're done. Running `bitbake` at this point will pick up the machine you've defined and run with it.

Note: `zc706-zynq7` is a machine defined in `meta-xilinx` for the evaluation board of the same name. A few common machines are:

- `zc702-zynq7`
- `zc706-zynq7`
- `zcu102-zynqmp`
- `gfex-prototype1b`
- `gfex-prototype2`
- `gfex-prototype3a`
- `gfex-prototype3b`
- `gfex-prototype4`

### What machines actually exist?

This is a strange question since it was not trivial for me at first. The way `bitbake` works is by looking in `<layer>/conf/machine/<machine-name>.conf` to find your machine. That machine will also be tied to device-tree files which are in `<layer>/conf/machine/boards/<machine-name>/*`, usually a directory of the same name (by habit, to make everyone's life easier).

For `meta-l1calo`, you simply look in `meta-l1calo/conf/machine` to see a list of available machines defined. Usually, you will need to define a new machine for a given device-tree specification -- a one-to-one mapping. As of the time of this writing, this is what currently happens.

Since it is expected that more than just me will be using this, you will need to think (I know right?) about the machine you want to use and then set that configuration yourself in the `conf/local.conf` file.

## Baking an Image

This is as simple as running

```
bitbake core-image-gfex
```

to build the `core-image-gfex` image for the gFEX boards.


## Flashing image using wic

For `gfex-prototype3` and `gfex-prototype4`, the default is to create a `wic` image. This can be used to flash an SD card, for example, to create the needed partition. This works for testing purposes but needs to be improved as the filesystem partition does not take up the rest of the space on the SD card automatically...

You can flash the SD card like so

```
sudo dd if=zynq-base-gfex-prototype4.wic of=/dev/sdX
```

where `sdX` can be identified by `diskutil list` on a mac or `fdisk -l` on a Linux machine. On Windows - you can use a special tool known as [Rufus](https://rufus.akeo.ie/) to do the same job.

# Extra Useful Commands

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
