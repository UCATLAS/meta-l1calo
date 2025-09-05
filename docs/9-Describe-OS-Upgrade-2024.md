# Description of the Xilinx 2024.2 Upgrade

I was handed this project in June 2025 in order to finish upgrading to the new SDT workflow. The last maintainer managed to make all the upgrades and compile the new OS, but she could not get it to boot. Here were the issues I faced in getting it to functionality:

1. **No Boot** - when I was handed this project, the OS made it past the FSBL and U-Boot, but crashed while the kernel was starting after these messages:

```shell
[    2.806378] mmc0: SDHCI controller on ff170000.mmc [ff170000.mmc] using ADMA 64-bit
[    2.814489] Waiting for root device /dev/mmcblk0p2...
[    4.043470] mmc0: Card removed during transfer!
[    4.047987] mmc0: Resetting controller.
```

After learning more about the workflow and many dead-ends, I compared the device trees in U-Boot between this OS and the last working version of the OS. I realized that there were extra nodes in the `pinctrl_sdhci1_default` node that weren't present in the old OS; if I removed them, the OS proceeded with the boot process but crashed at a later point in the boot. I found that this was also do to a `pinctrl` node that was in the new version, and after deleting it, I got the OS past booting!

After tracing back the source of these additions, I tracked it down to one option in `sdt.tcl` that included the layout of a different board. Once I deleted it, all the differences in the `pinctrl` nodes disappeared and the OS booted great (see `scripts/sdt.tcl` added in [this commit](https://github.com/UCATLAS/meta-l1calo/commit/14dcf4241b09a66d6e5c5d7dd9a5497149362e13#diff-47001d0e75da2506bff227213a6b859c5952bc0524482a16969016c17732a117) which used to have the `-board_dts zcu102-rev1.0` option in line 4).

2. **Switching from SysVinit to Systemd** - after getting the device booting, I was told that the custom boot scripts for the board were not running in this new OS, and that this new system uses Systemd instead of SysVinit for boot scripts. So, I migrated all of the boot scripts to Systemd (see [this commit](https://github.com/UCATLAS/meta-l1calo/commit/824156a8401a4af65d951570a4bc4aabc763258b)).

3. **Crashes from Read/Write with AXI Bus** - one set of the custom scripts that I was told still wasn't working was [gfex-management-scripts/RstgFEXgpio.sh](https://gitlab.cern.ch/atlas-l1calo/gfex/gfex-management-scripts/-/blob/master/RstgFEXgpio.sh). Specifically, the first line caused the following crash:

```shell
> devmem2 0xA0010010 w 0x00000000
/dev/mem opened.
Memory mapped at address 0xffffb091d000.
[ 150.699240] rcu: INFO: rcu_sched detected stalls on CPUs/tasks:
[ 150.705182] rcu: 0-...!: (0 ticks this GP) idle=2e0c/1/0x4000000000000000 softirq=7606/7606 fqs=0
[ 150.714141] rcu: (detected by 2, t=5256 jiffies, g=3865, q=52 ncpus=4)
[ 150.720754] Task dump for CPU 0:
[ 150.723972] task:devmem2 state:R running task stack:0 pid:388 ppid:382 flags:0x00000200
[ 150.733890] Call trace:
[ 150.736323] __switch_to+0xdc/0x154
[ 150.739813] 0xffffb091c000
```

After many dead-ends, I realized that any attempt of accessing a peripheral on the AXI bus would cause such a crash. I eventually found that this was because the AXI clock node (in my case, named `clocking0`) was missing from the Linux kernel device tree (`cortexa-linux.dts`), even though it was present in the FSBL and PMU device trees (`<MACHINE>-cortexa53-fsbl.dts` and `<MACHINE>-microblaze-pmu.dts`). After adding it back in manually, the crash no longer occured.

I then tried to track down why `cortexa-linux.dts` didn't generate with  `clocking0` while `<MACHINE>-cortexa53-fsbl.dts` and `<MACHINE>-microblaze-pmu.dts` did generate with that node. I realized that no matter how I configured the files, I couldn't get the device tree to have `clocking0`, even though all the other nodes from `tmp_sdt/std_outdir/pl.dtsi` end up in `cortexa-linux.dts`. Then, I found on the yocto forums that [other people had experienced this bug](https://lists.yoctoproject.org/g/meta-xilinx/topic/kernel_boot_disables_pl_clk/113571322) with `gen-machineconf`, and so I manually added `tmp_sdt/std_outdir/pl.dtsi` to the end of `cortexa-linux.dts` in the last line of `generate_device_tree.sh` (see [this commit](https://github.com/UCATLAS/meta-l1calo/commit/bb59df72a56955e2e860b9d26f190abee5dfbc0a)).

Other than some minor syntax changes and writing these documentation files, this has been my contribution to this repo.

\- Eyal N.


### Previous article: [Useful OS Commands](8-Useful-OS-Commands.md)
### Home:  [Documentation Overview](README.md)
### Next article: [TODOs for future upgrades](10-TODO-for-future.md)