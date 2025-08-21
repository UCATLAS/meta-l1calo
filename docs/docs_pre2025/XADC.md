The following file paths are used for XADC with the standard linux OS on top of the board

```
root@zc706-zynq7:~# ls /sys/devices/soc0/amba@0/f8007100.ps7-xadc/iio\:device0/
dev                        in_voltage2_vccbram_raw    in_voltage6_vrefp_scale
events                     in_voltage2_vccbram_scale  in_voltage7_vrefn_raw
in_temp0_offset            in_voltage3_vccpint_raw    in_voltage7_vrefn_scale
in_temp0_raw               in_voltage3_vccpint_scale  name
in_temp0_scale             in_voltage4_vccpaux_raw    power
in_voltage0_vccint_raw     in_voltage4_vccpaux_scale  sampling_frequency
in_voltage0_vccint_scale   in_voltage5_vccoddr_raw    subsystem
in_voltage1_vccaux_raw     in_voltage5_vccoddr_scale  uevent
in_voltage1_vccaux_scale   in_voltage6_vrefp_raw
```

The idea is each of these files represents, really, a stream. If you read the file once, it gives you the current value of that given register AT that given moment so you can continue to read and re-read it. For example:

```
cat /sys/devices/soc0/amba@0/f8007100.ps7-xadc/iio\:device0/in_temp0_scale
```

and if I wanted, say... the actual temperature

```
paste /sys/devices/soc0/amba@0/f8007100.ps7-xadc/iio\:device0/in_temp0_* | awk '{print ($1 + $2)*$3}'
```

which gives me the temperature in milliCelsius from using the three files: `in_temp0_offset`, `in_temp0_raw`, `in_temp0_scale` in that order.