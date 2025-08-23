# Compiling and Building Steps for meta-l1calo

In the words of the previous doc writer, "So you want to build an OS huh? Shit's hard". However, with yocto and the tools provided by Xilinx, we don't have to get too deep in writing kernel code. Just following the following steps will get you up and running with an image of the OS within the day.

## Host Operating System

Right now, the build process only has been tested for Ubuntu Linux computers (The only Linux distros Vitis supports are Ubuntu and Redhat). These versions have been tested:

| Host Operating System | Version | Works? | Notes |  
| :--- | :-- | :-- | :-- 
| Linux Ubuntu | 22.04 | Yes |  |
| Linux Ubuntu | 24.04 | Yes* | Ubuntu 24.04 is not yet supported by the long-term version of yocto we're using as of August 2025 ([Scarthgap](https://wiki.yoctoproject.org/wiki/Releases)), so there is a bug when using bitbake. After the **Software Prerequisites** section, use `sudo apparmor_parser -R /etc/apparmor.d/unprivileged_userns` in the terminal (see [this discussion on the bug](https://lists.yoctoproject.org/g/yocto/topic/workaround_for_uid_map_error/106192359)) |

## Software Prerequisites

The following must be downloaded and set up before beginning the build process:

- **Xilinx Vitis 2024.2** (either as part of a Xilinx Vitis installation, or in the smaller Vitis Embedded installation)
	- If it's not already installed, here are the installation steps:
		1. Get the Linux Self-Extracting Vivado 2024.2 installer from [AMD's website](https://www.xilinx.com/support/download/index.html/content/xilinx/en/downloadNav/vivado-design-tools/2024-2.html)
		2. Using a terminal, write `sudo chmod a+x <Vivado_Installer>.bin` replacing what's in the brackets with the name of the installer, and then run it with `sudo ./<Vivado_Installer>.bin`
		3. Authenticate your AMD account, choose your corresponding installation (e.g. Vitis Embedded), and then where you want to install it (for example, I had it installed in `/local/code`). Once you hit install, the process for Vitis Embedded can take around an hour (and use around 40 GB).
		4. Just like you are prompted, run `/<Path_to_Vitis>/Xilinx_2024.2/Vitis/2024.2/scripts/installLibs.sh`
		5. Then edit ~/.bashrc (by using the command `open ~/.bashrc`) and add this line to the end: `source "/<Path_to_Vitis>/Xilinx_2024.2/Vitis/2024.2/settings64.sh"`. This will add the Vitis commands to your PATH for every terminal session.
	- In order to test if you have this installed correctly, run `xsct` in a newly-opened terminal. If it runs correctly and you are in an interactive terminal, you're all set! (type `exit` to leave this interactive `xsct` session)
- **Install the correct package dependencies**: in a terminal, run:
```shell
sudo apt install gawk wget git diffstat unzip texinfo gcc build-essential chrpath socat cpio python3 python3-pip curl lz4
```
- **Set up SSH for Github**:
	1.  Create an SSH key pair with the  `ssh-keygen -t ed25519 -C "your_email@example.com"` command in a terminal. Choose the default file for saving this key, as well as a passphrase.
	2. Use the `ssh-add` command in the terminal to add your new ssh key.
	3.  Copy the value of the public SSH key (the output of the command `cat ~/.ssh/id_ed25519.pub`) to the clipboard
	4. On [Github](github.com), go to Settings → Access → SSH and GPG keys. Click the "New SSH key" button, and add a label for and the contents of your ssh key.
	5. Set your name and email for git:
	```sh
	git config --global user.email "you@example.com"
	git config --global user.name "Your Name"
	```
	- In order to test if you have this set up correctly, try `ssh -T git@github.com`. You should see:
```shell
> Hi USERNAME! You've successfully authenticated, but GitHub does not provide shell access.
```
- **Set up SSH for CERN's Gitlab**:

	1.  Copy the value of the same public SSH key generated in the previous Github ssh step (the output of the command `cat ~/.ssh/id_ed25519.pub`) to the clipboard
	2. Sign in to [gitlab.cern.ch](gitlab.cern.ch), go to Preferences → SSH keys. Click the "Add new key" button, and add a title for and the contents of your ssh key.
	- In order to test if you have this set up correctly, try `ssh git@gitlab.cern.ch -T -p 7999`. You should see:
```shell
> Welcome to GitLab, USERNAME!
```

## Building the OS

You are now ready to construct the source files with the setup scripts and begin building! Note that this can take multiple hours and end up using around 140 GB. Please follow the proceeding steps in a terminal which is in the folder where you would like the project to be located:

1. Download the [setup.sh](../scripts/setup.sh) file:
```shell
wget https://raw.githubusercontent.com/UCATLAS/meta-l1calo/refs/heads/master/scripts/setup.sh
```
2. Run the script:
```shell
source setup.sh
```
3. Run device tree generating script, choosing the appropriate target machine (note that when running for the first time, this can take many minutes):
```shell
source generate_device_tree.sh
```
4. Run bitbake to generate the OS image (note that when running for the first time, this can take multiple hours):
```shell
bitbake core-image-gfex
```
5. Copy the OS image to `xilinx_bitbake/output`:
```shell
source copy_output.sh
```
6. Write the OS image from `xilinx_bitbake/output` to the correct SD card:
```shell
source write_output_to_sd.sh
```

...and there you go! You should now be able to move the SD card to the gFEX board and begin testing.

### Previous article: [Introduction to Building an OS](1-Intro-to-Build-OS.md)
### Home: [Documentation Overview](README.md)
### Next article: [Structure of the Source Code](3-Source-Code-Structure.md)