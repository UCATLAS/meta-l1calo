# Introduction to Building an OS
An **operating system** (OS) is the basic system software that manages a computer: it executes programs, manages memory and controls peripherals. Common examples are Windows, MacOS, and the different Linux distributions.

The OS we are building is for the **System on Chip** (SoC) of the gFEX board, a complete computer system integrated onto a single chip, combining various components like the CPU, memory, and input/output (IO) peripherals. Most importantly, the SoC on the gFEX board (an [AMD Zynq™ UltraScale+™ MPSoC](https://www.amd.com/en/products/adaptive-socs-and-fpgas/soc/zynq-ultrascale-plus-mpsoc.html), often shortened to ZynqMP in the code) communicates with the FPGAs on the board.

Since it is open-source, Xilinx (now under AMD) chose a version of Linux named [PetaLinux](https://www.xilinx.com/publications/xcellonline/Xcell90_p46.pdf), that can be built upon and customized according to the needs of the board.

## Yocto

The tool we use to build our custom version of PetaLinux is the [Yocto Project](https://www.yoctoproject.org). Yocto is a tool to build an OS in layers, so we only need to configure the parts that are custom to our OS, and then have the standard linux environment underneath. These layers are usually named `meta-<layer_name>` (such as our layer named `meta-l1calo`, since it is used for the L1 calorimeter). These layers are found in [open-embedded](http://www.openembedded.org), which is an entire open-source community dedicated to embedded OS solutions. 

## Open-Embedded Layers

The open-embedded infrastructure works using layers, emphasizing package dependency resolution and management. Each layer includes specific functionality for the OS, containing recipes for how to add the necessary packages into the OS when it builds an image.

You can go [here to see a list of layers](http://layers.openembedded.org/layerindex/branch/master/layers) that are supported by the open-embedded community. For this project, the included [setup.sh](../scripts/setup.sh) script installs all the needed layers other than meta-l1calo from [Xilinx's list of manifests](https://github.com/Xilinx/yocto-manifests/blob/rel-v2024.2/default.xml) to setup the Yocto build system for their devices. This OS project includes:

- [meta-openembedded](http://layers.openembedded.org/layerindex/branch/master/layer/meta-oe)
- [meta-xilinx](http://layers.openembedded.org/layerindex/branch/master/layer/meta-xilinx-core)
- [meta-petalinux](https://github.com/Xilinx/meta-petalinux)
- [meta-python](http://layers.openembedded.org/layerindex/branch/master/layer/meta-python)
- [meta-l1calo](https://github.com/UCATLAS/meta-l1calo)

You can run `bitbake-layers show-layers` to show what layers are in your yocto project. For example, after building the OS, these are the used layers:

```
layer                   path                                                                    priority
========================================================================================================
core                    sources/poky/meta           												5
yocto                   sources/poky/meta-poky   													5
xilinx-microblaze       sources/poky/../meta-xilinx/meta-microblaze   								5
xilinx                  sources/poky/../meta-xilinx/meta-xilinx-core  								5
xilinx-standalone       sources/poky/../meta-xilinx/meta-xilinx-standalone 							7
xilinx-standalone-sdt   sources/poky/../meta-xilinx/meta-xilinx-standalone-sdt  					7
xilinx-bsp              sources/poky/../meta-xilinx/meta-xilinx-bsp  								5
xilinx-vendor           sources/poky/../meta-xilinx/meta-xilinx-vendor  							5
xilinx-virtualization   sources/poky/../meta-xilinx/meta-xilinx-virtualization 						5
xilinx-mali400          sources/poky/../meta-xilinx/meta-xilinx-mali400  							5
xilinx-demos            sources/poky/../meta-xilinx/meta-xilinx-demos  								5
xilinx-multimedia       sources/poky/../meta-xilinx/meta-xilinx-multimedia  						5
xilinx-tools            sources/poky/../meta-xilinx-tools  											8
amd-adaptive-socs-core  sources/poky/../meta-amd-adaptive-socs/meta-amd-adaptive-socs-core			6
amd-adaptive-socs-bsp   sources/poky/../meta-amd-adaptive-socs/meta-amd-adaptive-socs-bsp  			6
meta-arm                sources/poky/../meta-arm/meta-arm  											5
arm-toolchain           sources/poky/../meta-arm/meta-arm-toolchain  								5
perl-layer              sources/poky/../meta-openembedded/meta-perl  								5
meta-python             sources/poky/../meta-openembedded/meta-python  								5
filesystems-layer       sources/poky/../meta-openembedded/meta-filesystems  						5
gnome-layer             sources/poky/../meta-openembedded/meta-gnome  								5
multimedia-layer        sources/poky/../meta-openembedded/meta-multimedia  							5
networking-layer        sources/poky/../meta-openembedded/meta-networking  							5
webserver               sources/poky/../meta-openembedded/meta-webserver  							5
xfce-layer              sources/poky/../meta-openembedded/meta-xfce  								5
meta-initramfs          sources/poky/../meta-openembedded/meta-initramfs  							5
openembedded-layer      sources/poky/../meta-openembedded/meta-oe  									5
xilinx-contrib          sources/poky/../meta-xilinx/meta-xilinx-contrib  							5
virtualization-layer    sources/poky/../meta-virtualization  										8
kria                    sources/poky/../meta-kria   												5
embedded-plus           sources/poky/../meta-embedded-plus  										5
security                sources/poky/../meta-security  												8
tpm-layer               sources/poky/../meta-security/meta-tpm  									6
xilinx-tsn              sources/poky/../meta-xilinx-tsn  											6
petalinux               sources/poky/../meta-petalinux  											6
openamp-layer           sources/poky/../meta-openamp  												5
qt5-layer               sources/poky/../meta-qt5    												7
meta-aws                sources/poky/../meta-aws    												6
jupyter-layer           sources/poky/../meta-jupyter  												7
rauc                    sources/poky/../meta-rauc   												6
meta-system-controller  sources/poky/../meta-system-controller  									6
ros-common-layer        sources/poky/../meta-ros/meta-ros-common  									10
ros2-layer              sources/poky/../meta-ros/meta-ros2  										11
ros2-jazzy-layer        sources/poky/../meta-ros/meta-ros2-jazzy  									12
l1calo                  sources/meta-l1calo    														14
```

where everything is ordered by its priority during compilation (higher number = lower priority). We want `meta-l1calo` to depend on all the above packages and so we set it to a low priority (here, it's 14).

## Building, Flashing, and Testing
[Bitbake](https://docs.yoctoproject.org/bitbake/bitbake-user-manual/bitbake-user-manual-intro.html) is the tool used by yocto for compiling all of these layers. You use `bitbake core-image-gfex` to complete a build of this OS. Metadata is stored in recipe (`.bb`) and related recipe “append” (`.bbappend`) files, configuration (`.conf`) and underlying include (`.inc`) files, and in class (`.bbclass`) files. The metadata provides BitBake with instructions on what tasks to run and the dependencies between those tasks

Once the OS image is built, we need to have it so that the SoC can boot into the OS: we need to write it onto an SD card. The SD card will need two partitions:

| Partition | Name | Filesystem Type | Filesystem Partition ID | Included Files |  Notes |  
| :--- | :--: | :--: | :--: | :--- | :--- |  
| 1 | boot | FAT32 | 0x0c | <li>boot.bin</li><li>system.dtb</li><li>Image</li><li>boot.scr</li><li>u-boot.bin</li>| The *boot* partition, which includes the instructions for the OS (specifically, the kernel) to boot |
| 2 | root | Linux | 0x83 | Everything in the \<MACHINE\>.rootfs.ext4 filesystem | The partition which stores the filesystem that the user manipulates when the OS is running |

The `bitbake` process creates all the necessary files list in the table above. Then, you can either manually partition the SD cards (see [format_sd_card.sh](../scripts/format_sd_card.sh)) and copy the correct files into the correct partition (see [write_output_to_sd_manually.sh](../scripts/write_output_to_sd_manually.sh)), or you can just use the .wic image. When bitbake finishes, it creates a .wic file that already includes both partitions and the correct files within them. This image can just be written directly to the sd card (see [write_output_to_sd.sh](../scripts/write_output_to_sd.sh)).

Then, all you need to do is put the SD card into the board and power it on. Good luck!

### Home: [Documentation Overview](README.md)
### Next article: [Compiling and Building Steps](2-Compiling-and-Building-Steps.md)