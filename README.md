# meta-l1calo

## Introduction

This layer is intended to be the home of l1calo modules for OpenEmbedded.
Modules in this layer need to be independent of openembedded-core and
meta-openembedded/meta-oe.

## Quick Build

First, make sure you have Xilinx Vitis 2024 installed as well as ssh set up for Github and CERN's Gitlab. Then, get the following necessary dependencies for running bitbake on your ubuntu host machine:

```sh
sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip
```

Copy the script.sh file found at scripts/setup.sh onto your machine and run a terminal in its directory:

```sh
    source setup.sh
    source generate_device_tree.sh
    bitbake core-image-gfex
    source copy_output.sh
    source write_output_to_sd.sh
```

The ```source setup.sh``` commmand should create the xilinx_bitbake folder and automatically download all the necessary layers for yocto into the xilinx_bitbake/sources folder (including the meta-l1calo layer found in this repo). Note that when running for the first time, generate_device_tree.sh can take many minutes and bitbake core-image-gfex will take a very long time (i.e. multiple hours).

**For more in-depth instructions of the building process, visit [this repo's documentation](docs/2-Compiling-and-Building-Steps.md)**


## Dependencies

The meta-l1calo layer depends on:

	URI: git://git.openembedded.org/openembedded-core
	layers: meta
	branch: master
	revision: HEAD

	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-oe
	branch: master
	revision: HEAD

	URI: git://git.yoctoproject.org/meta-xilinx
	layers: meta-xilinx
	branch: master
	revision: HEAD

	URI: git://git.openembedded.org/meta-openembedded
	layers: meta-python
	branch: master
	revision: HEAD

Please follow the recommended setup procedures of your OE distribution.

## Contributing

The meta-openembedded mailinglist
(openembedded-devel@lists.openembedded.org) is used for questions,
comments and patch review. It is subscriber only, so please register
before posting.

Send pull requests to openembedded-devel@lists.openembedded.org with
'[meta-l1calo]' in the subject.

When sending single patches, please use something like:
'git send-email -M -1 --to=openembedded-devel@lists.openembedded.org --subject-prefix=meta-l1calo][PATCH'

## Maintenance

Maintainers:
  - Giordon "kratsg" Stark <gstark@cern.ch>
  - Emily Smith <emily.ann.smith@cern.ch>
