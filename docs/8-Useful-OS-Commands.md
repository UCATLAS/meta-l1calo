# Useful OS Commands

The OS is running a version of Linux, so all of the linux commands discussed previously apply. Here are the relevant commands for testing within u-boot and the OS:

### U-Boot Commands

U-Boot is an embedded systems bootloader that runs before the kernel starts. On the serial connection, once the device powers on, hold any key to stop the autoboot from moving from U-Boot to the Linux kernel. Then, you have a U-Boot console that allows for testing your setup before the kernel starts (which is especially helpful if the system is crashing while booting):

| Command Name | Notes |
|:--|:--|
| <pre><code>load mmc 0:1 ${fdt_addr_r} system.dtb</code>&#13;<code>fdt addr ${fdt_addr_r}</code>&#13;<code>fdt print /axi/mmc@ff170000</code> </pre>  | Use these three lines to print a node in the device tree to make sure it's correct. |
| <pre><code>md 0xa0010010 1</code>&#13;<code>mw 0xa0010010 0x00000000</code> </pre> | Use the first line to read, and the second line to write, to a certain memory address. Here, I’m manipulating the memory address 0xa0010010. |

### OS Commands

Once the OS has booted, you can use the following commands:

| Command Name | Notes |
|:--|:--|
| `systemctl list-units --type=service --legend=false` | List systemd processes running. |
| `journalctl -u [proc].service` | See log of systemd process output. |
| `ifconfig` | Check status of network ports (like ethernet). |
| `strings /proc/device-tree/axi/M00_AXI@a0010000/status` | Check status property of Linux device tree node. |
| `zcat /proc/config.gz` | grep CONFIG_ARCH_HAS_DEVMEM_IS_ALLOWED` | Check if linux configuration is present. |
| `cat /proc/iomem` | Displays a map of the system's physical memory, showing which memory address ranges are reserved and by what. |
| `cd /sys/class/` | Looking into device driver files. |
| `cat /sys/kernel/debug/clk/clk_summary` | Shows a summary of the active clocks. |

### Previous article: [Structure of the Final OS](7-Final-OS-Structure.md)
### Home: [Documentation Overview](README.md)
### Next article: [Description of the Vivado 2024.2 Upgrade](9-Describe-OS-Upgrade-2024.md)