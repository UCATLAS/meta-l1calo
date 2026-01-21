<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Building the Device Tree Source (dts)](#building-the-device-tree-source-dts)
  - [To Do after Auto-Generating](#to-do-after-auto-generating)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Building the Device Tree Source (dts)

Follow the instructions here: http://www.wiki.xilinx.com/Build+Device+Tree+Blob

You will only need to generate the `*.dts` and `*.dtsi` files. The `bitbake` toolchain will compile these into `*.dtb` files for you, as well as compile `u-boot` with the custom device-tree you're using. When I auto-generated, it produced 4 files for me:

- pl.dtsi
- skeleton.dtsi
- system.dts
- zynq-7000.dtsi

The `system.dts` file, in my example, was the top-level one. When all four are compiled, it will produce a `system.dtb` for the board which we copy over and rename `devicetree.dtb` (or use an `uEnv.txt` file).

## To Do after Auto-Generating

Note that when Xilinx auto-generates the device tree source files for you, it won't connect up the PHY chips on the board... because it doesn't know which PHY chips you're using! When you use the SDK and use the supplied `.C` files from Xilinx, it contains plenty of if/else statements to look up which PHY chip you're using and adjust the function calls based on that. With an OS, we need to know what PHY chip we use when we load up the device tree at boot time - entirely different.

In order to add the PHY chip, I looked in `system.dts` and added some lines so that

```dts
 &gem0 {
 	enet-reset = <&gpio0 51 0>;
 	local-mac-address = [00 0a 35 00 00 00];
 	phy-mode = "rgmii-id";
 	status = "okay";
  	xlnx,ptp-enet-clock = <0x69f6bcb>;
  };
```

became

```dts
 &gem0 {
 	enet-reset = <&gpio0 51 0>;
 	local-mac-address = [00 0a 35 00 00 00];
 	phy-mode = "rgmii-id";
 	status = "okay";
  	xlnx,ptp-enet-clock = <0x69f6bcb>;
  	phy-handle = <&phy0>;
	phy0: phy@23 {
		compatible = "marvell,88e1116r";
		device_type = "ethernet-phy";
		reg = <0x17>;
  	};
  };
```

as seen in this change https://github.com/UCATLAS/meta-l1calo/compare/7f7687...69d89d1 .

[1] http://www.wiki.xilinx.com/Linux+Drivers