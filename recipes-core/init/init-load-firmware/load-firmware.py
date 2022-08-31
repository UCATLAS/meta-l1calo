#!/usr/bin/env python3

import os, subprocess


ENABLE_FIRMWARE_LOAD = False


if ENABLE_FIRMWARE_LOAD:
    
    load = "not empty" 
    while (load != ""):

        prep = subprocess.getoutput("echo 0 > /sys/class/fpga_manager/fpga0/flags")
        load = subprocess.getoutput("echo zfpga_top.bit > /sys/class/fpga_manager/fpga0/firmware")
        print(load)

    print("Loaded firmware on zFPGA using /lib/firmware/zfpga_top.bit")

else:
    print("Did not load any firmware on the zFPGA")

