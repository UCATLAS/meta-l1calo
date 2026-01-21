# TODOs for Future Updates

## Vivado/Vitis 2025.1

1. Whenever `xsct` is run, it outputs the following message:
```shell
Warning: XSCT is deprecated and will be removed in future releases.
We recommend using the new Python command-line tool for project management and debugging:
- Run "vitis -i" for interactive mode.
- Run "vitis -s <script>" for script mode.
```
In 2025.1, [AMD will deprecate `xsct` and make SDTGen its own executable](https://xilinx-wiki.atlassian.net/wiki/spaces/A/pages/3172532226/Understanding+the+Relationship+Between+SDTGen+and+XSCT). Thus, we should remake that part of the `generate_device_tree.sh` script to use SDTGen on its own when the update occurs.

## Future Yocto

1. If the [`clocking0` bug](https://lists.yoctoproject.org/g/meta-xilinx/topic/kernel_boot_disables_pl_clk/113571322) with `gen-machineconf` that was mentioned in the previous page is fixed, `generate_device_tree.sh` should be updated.

### Previous article: [Description of the Vivado 2024.2 Upgrade](9-Describe-OS-Upgrade-2024.md)
### Home: [Documentation Overview](README.md)