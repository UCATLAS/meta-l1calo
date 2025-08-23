# Documentation for meta-l1calo

This documentation serves to provide a high-level overview of how this layer is used to build a custom operating system (OS) for the gFEX boards at the ATLAS experiment.

It's categorized into the following documentation files for easier future reference:

- [Introduction to Building an OS: yocto, bitbake, and petalinux](1-Intro-to-Build-OS.md)
- Source Code
	- [Compiling and Building Steps](2-Compiling-and-Building-Steps.md)
	- [Structure of the Source Code, including this layer](3-Source-Code-Structure.md)
	- [Useful Linux Commands](4-Useful-Linux-Commands.md)
- Hardware Setup
	- [Physical Hardware Setup with an ATCA shelf](5-Physical-Hardware-Setup.md)
	- [Useful ATCA Commands](6-Useful-ATCA-Commands.md)
- Final OS
	- [Structure of the OS, including custom programs](7-Final-OS-Structure.md)
	- [Useful OS Commands](8-Useful-OS-Commands.md)
- Notes on Development
	- [Description of the Xilinx 2024.2 Upgrade](9-Describe-OS-Upgrade-2024.md)
	- [TODOs for future upgrades](10-TODO-for-future.md)

For archiving purposes, the documentation from before the 2024.2 Upgrade is kept in the [docs_pre2025 folder](docs_pre2025).