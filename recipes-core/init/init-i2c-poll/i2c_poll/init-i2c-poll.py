#!/usr/bin/env python3
# Copyright 2021 Emily Smith and Weigang Yin Permission is hereby granted, free of charge, to
# any person obtaining a copy of this software and associated documentation
# files (the "Software"), to deal in the Software without restriction,
# including without limitation the rights to use, copy, modify, merge, publish,
# distribute, sublicense, and/or sell copies of the Software, and to permit
# persons to whom the Software is furnished to do so, subject to the following
# conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.


import json, os, argparse, datetime
from gfex_minipods import *
from gfex_power import *
from gfex_temperature import *
from periphery import I2C

parser = argparse.ArgumentParser(description='Read I2C Sensors')
parser.add_argument('--log', nargs='?', dest='logfile', action='store', const='i2c_log', default='', help='Mask A with specific configuration')
args = parser.parse_args()

i2c_file = '/logs/i2c_hardware.json'

#Get rid of old file frome before startup
if os.path.exists(i2c_file):
  os.remove(i2c_file)

###############
## CONSTANTS ##
###############

TCA9548_U93_ADDR = 0x70 #1110_000X  0XE0

#I2C Bus Addresses
SENSOR_IIC_BUS   = 0x01
Z_IIC_BUS2       = 0x02
Z_IIC_BUS3       = 0x04
Z_IIC_BUS4       = 0x08
Z_IIC_BUS5       = 0x10
MGT_IIC_BUS      = 0x80

AD7414_U82_ADDR  = 0x49 #1001_001X  0X92
AD7414_U83_ADDR  = 0x4A #1001_010X  0X94
AD7414_U84_ADDR  = 0x48 #1001_000X  0X90
AD7414_U87_ADDR  = 0x4D #1001_101X  0X9A

ADM1066_U52_ADDR = 0x34 #0110_100X  0X68
ADM1066_U51_ADDR = 0x35 #0110_101X  0X6A

LTC2499_U2_ADDR  = 0x15 #0010_101X  0X2A
LTC2499_U1_ADDR  = 0x14 #0010_100X  0X28

BMR4582_U11_ADDR = 0x7F #1111_111X  0XFE
INA226_U81_ADDR  = 0x40 #1000_000X  0X80

# I2C BUS2 Addresses: Zynq MiniPODs 
MPOD_U3_ADDR     = 0x28 #0101_000X  0X50
MPOD_U24_ADDR    = 0x29 #0101_001X  0X52
MPOD_U56_ADDR    = 0x2A #0101_010X  0X54
MPOD_U72_ADDR    = 0x30 #0110_000X  0X60
MPOD_U91_ADDR    = 0x31 #0110_001X  0X62

# I2C BUS5 Addresses: pFPGA A MiniPODs
MPOD_U32_ADDR    = 0x28 #0101_000X  0X50
MPOD_U25_ADDR    = 0x29 #0101_001X  0X52
MPOD_U96_ADDR    = 0x30 #0110_000X  0X60
MPOD_U102_ADDR   = 0x31 #0110_001X  0X62
MPOD_U103_ADDR   = 0x32 #0110_010X  0X64
MPOD_U104_ADDR   = 0x33 #0110_011X  0X66
MPOD_U105_ADDR   = 0x34 #0110_100X  0X68
MPOD_U106_ADDR   = 0x35 #0110_101X  0X6A
MPOD_U107_ADDR   = 0x36 #0110_110X  0X6C
MPOD_U97_ADDR    = 0x37 #0110_111X  0X6E

# I2C BUS4 Addresses: pFPGA B MiniPODs
MPOD_U33_ADDR    = 0x28 #0101_000X  0X50
MPOD_U27_ADDR    = 0x29 #0101_001X  0X52
MPOD_U98_ADDR    = 0x30 #0110_000X  0X60
MPOD_U100_ADDR   = 0x31 #0110_001X  0X62
MPOD_U101_ADDR   = 0x32 #0110_010X  0X64
MPOD_U108_ADDR   = 0x33 #0110_011X  0X66
MPOD_U109_ADDR   = 0x34 #0110_100X  0X68
MPOD_U111_ADDR   = 0x35 #0110_101X  0X6A
MPOD_U112_ADDR   = 0x36 #0110_110X  0X6C
MPOD_U113_ADDR   = 0x37 #0110_111X  0X6E

# I2C BUS3 Addresses: pFPGA C MiniPODs
MPOD_U34_ADDR    = 0x28 #0101_000X  0X50
MPOD_U42_ADDR    = 0x29 #0101_001X  0X52
MPOD_U114_ADDR   = 0x30 #0110_000X  0X60
MPOD_U115_ADDR   = 0x31 #0110_001X  0X62
MPOD_U116_ADDR   = 0x32 #0110_010X  0X64
MPOD_U117_ADDR   = 0x33 #0110_011X  0X66
MPOD_U118_ADDR   = 0x34 #0110_100X  0X68
MPOD_U119_ADDR   = 0x35 #0110_101X  0X6A
MPOD_U120_ADDR   = 0x36 #0110_110X  0X6C
MPOD_U90_ADDR    = 0x37 #0110_111X  0X6E


###############
### METHODS ###
###############

def set_i2c_mux(dev_addr,channel):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(dev_addr, [I2C.Message([channel])])
  i2c.close()

def eeprom_reg_read(dev_addr,reg_addrh,reg_addrl,nbits):
    i2c = I2C("/dev/i2c-1")
    i2c.transfer(dev_addr, [I2C.Message([reg_addrh,reg_addrl])])# Reg for read
    read = I2C.Message([0x0]*nbits, read=True)
    i2c.transfer(dev_addr, [read])
    i2c.close()
    return read.data[0]

def get_hardware_id():
  set_i2c_mux(TCA9548_U93_ADDR, MGT_IIC_BUS)
  BoardID=[0,0,0,0,0,0,0,0,0,0,0,0,0]
  for i in range (0,13,1):
    BoardID[i] = eeprom_reg_read(0x50,0x00,i,1)
  BoardName = ''.join(chr(x) for x in BoardID)
  return int('0x' + BoardName[-2:],16)

def handle_errors(f):
  try:
    return f()
  except: 
    return 1




#####################
# Define Dictionary #
#####################

print("Beginning constant i2c sensor polling.")

hardware = {}
hardware['TEMPSENSORS'] = {}
hardware['POWERMODULES'] = {}
hardware['MINIPODS'] = {}

#Temperature Sensors
hardware['TEMPSENSORS']['U82'] = {}
hardware['TEMPSENSORS']['U83'] = {}
hardware['TEMPSENSORS']['U84'] = {}
hardware['TEMPSENSORS']['U87'] = {}

#DCDC
hardware['POWERMODULES']['U11_12V'] = {}
hardware['POWERMODULES']['U11_48V'] = {}
hardware['POWERMODULES']['U81'] = {} # external device for U11

#Power Modules
hardware['POWERMODULES']['Z_U66_DDR4'] = {}
hardware['POWERMODULES']['Z_U66_MGTAVTT'] = {}
hardware['POWERMODULES']['Z_U73'] = {}
hardware['POWERMODULES']['Z_U73_INT'] = {}
hardware['POWERMODULES']['Z_U55'] = {}
hardware['POWERMODULES']['Z_U59'] = {}
hardware['POWERMODULES']['Z_U59_MGTAVCC'] = {}
hardware['POWERMODULES']['A_U122'] = {}
hardware['POWERMODULES']['A_U77'] = {}
hardware['POWERMODULES']['A_U30'] = {}
hardware['POWERMODULES']['B_U123'] = {}
hardware['POWERMODULES']['B_U124'] = {}
hardware['POWERMODULES']['B_U40'] = {}
hardware['POWERMODULES']['C_U126'] = {}
hardware['POWERMODULES']['C_U125'] = {}
hardware['POWERMODULES']['C_U44'] = {}

#MiniPODs
hardware['MINIPODS']['RX_CALO_S_U91'] = {}
hardware['MINIPODS']['TX_FELIX_Z_U3'] = {}
hardware['MINIPODS']['TX_L1TOPO_Z_U24'] = {}
hardware['MINIPODS']['TX_GLOBAL_Z_U56'] = {}
hardware['MINIPODS']['RX_FELIX_Z_U72'] = {}
hardware['MINIPODS']['TX_L1TOPO_A_U32'] = {}
hardware['MINIPODS']['TX_L1TOPO_A_U25'] = {}
hardware['MINIPODS']['RX_CALO_A_U96'] = {}
hardware['MINIPODS']['RX_CALO_A_U102'] = {}
hardware['MINIPODS']['RX_CALO_A_U103'] = {}
hardware['MINIPODS']['RX_CALO_A_U104'] = {}
hardware['MINIPODS']['RX_CALO_A_U105'] = {}
hardware['MINIPODS']['RX_CALO_A_U106'] = {}
hardware['MINIPODS']['RX_CALO_A_U107'] = {}
hardware['MINIPODS']['RX_CALO_A_U27'] = {}
hardware['MINIPODS']['RX_CALO_A_U97'] = {}
hardware['MINIPODS']['TX_L1TOPO_B_U33'] = {}
hardware['MINIPODS']['TX_L1TOPO_B_U27'] = {}
hardware['MINIPODS']['RX_CALO_B_U98'] = {}
hardware['MINIPODS']['RX_CALO_B_U100'] = {}
hardware['MINIPODS']['RX_CALO_B_U101'] = {}
hardware['MINIPODS']['RX_CALO_B_U108'] = {}
hardware['MINIPODS']['RX_CALO_B_U109'] = {}
hardware['MINIPODS']['RX_CALO_B_U111'] = {}
hardware['MINIPODS']['RX_CALO_B_U112'] = {}
hardware['MINIPODS']['RX_CALO_B_U113'] = {}
hardware['MINIPODS']['TX_L1TOPO_C_U34'] = {}
hardware['MINIPODS']['TX_L1TOPO_C_U42'] = {}
hardware['MINIPODS']['RX_CALO_C_U114'] = {}
hardware['MINIPODS']['RX_CALO_C_U115'] = {}
hardware['MINIPODS']['RX_CALO_C_U116'] = {}
hardware['MINIPODS']['RX_CALO_C_U117'] = {}
hardware['MINIPODS']['RX_CALO_C_U118'] = {}
hardware['MINIPODS']['RX_CALO_C_U119'] = {}
hardware['MINIPODS']['RX_CALO_C_U120'] = {}
hardware['MINIPODS']['RX_CALO_C_U90'] = {}

# Only Do Once 
hardware['ID'] = get_hardware_id()

while True: 
    # set mux to SENSOR I2C BUS
    set_i2c_mux(TCA9548_U93_ADDR,SENSOR_IIC_BUS)

    ######################################################
    #################### TEMPERATURES ####################
    ######################################################

    # Board temperature sensors AD7414
    
    hardware['TEMPSENSORS']['U82']['TEMP'] = handle_errors( lambda: ad7414_mon(AD7414_U82_ADDR))
    hardware['TEMPSENSORS']['U83']['TEMP'] = handle_errors( lambda: ad7414_mon(AD7414_U83_ADDR))
    hardware['TEMPSENSORS']['U84']['TEMP'] = handle_errors( lambda: ad7414_mon(AD7414_U84_ADDR))
    hardware['TEMPSENSORS']['U87']['TEMP'] = handle_errors( lambda: ad7414_mon(AD7414_U87_ADDR))
    
    # # 12V DCDV BMR458 temperature
    hardware['POWERMODULES']['U11_12V']['TEMP'] = handle_errors( lambda: lin5_11ToFloat(bmr458_mon(BMR4582_U11_ADDR,0x8D)))
    hardware['POWERMODULES']['U11_48V']['TEMP'] = handle_errors( lambda: lin5_11ToFloat(bmr458_mon(BMR4582_U11_ADDR,0x8D)))

    # LTM4630A power module temperatures
    hardware['POWERMODULES']['Z_U66_DDR4']['TEMP']    = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xBC))
    hardware['POWERMODULES']['Z_U66_MGTAVTT']['TEMP'] = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xBC))
    hardware['POWERMODULES']['Z_U73']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB5))
    hardware['POWERMODULES']['Z_U73_INT']['TEMP']     = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB5))
    hardware['POWERMODULES']['Z_U55']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xBD) )
    hardware['POWERMODULES']['Z_U59']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB6) )
    hardware['POWERMODULES']['Z_U59_MGTAVCC']['TEMP'] = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB6))
    hardware['POWERMODULES']['A_U122']['TEMP']        = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB0))
    hardware['POWERMODULES']['A_U77']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB8))
    hardware['POWERMODULES']['A_U30']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB1))
    hardware['POWERMODULES']['B_U123']['TEMP']        = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB9))
    hardware['POWERMODULES']['B_U124']['TEMP']        = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB2))
    hardware['POWERMODULES']['B_U40']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xBA))
    hardware['POWERMODULES']['C_U126']['TEMP']        = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB3))
    hardware['POWERMODULES']['C_U125']['TEMP']        = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xBB))
    hardware['POWERMODULES']['C_U44']['TEMP']         = handle_errors( lambda: ltc2499_temp_mon(LTC2499_U2_ADDR,0x90,0xB4))

    # MiniPOD temperatures
    hardware['MINIPODS']['RX_CALO_S_U91']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS2,MPOD_U91_ADDR))

    hardware['MINIPODS']['TX_FELIX_Z_U3']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS2,MPOD_U3_ADDR) )
    hardware['MINIPODS']['TX_L1TOPO_Z_U24']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS2,MPOD_U24_ADDR))
    hardware['MINIPODS']['TX_GLOBAL_Z_U56']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS2,MPOD_U56_ADDR))
    hardware['MINIPODS']['RX_FELIX_Z_U72']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS2,MPOD_U72_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_A_U32']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U32_ADDR)  )
    hardware['MINIPODS']['TX_L1TOPO_A_U25']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U25_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U96']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U96_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U102']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U102_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U103']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U103_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U104']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U104_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U105']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U105_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U106']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U106_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U107']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U107_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U27']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U27_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U97']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS5,MPOD_U97_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_B_U33']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U33_ADDR)  )
    hardware['MINIPODS']['TX_L1TOPO_B_U27']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U27_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U98']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U98_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U100']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U100_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U101']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U101_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U108']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U108_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U109']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U109_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U111']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U111_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U112']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U112_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U113']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS4,MPOD_U113_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_C_U34']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U34_ADDR) )
    hardware['MINIPODS']['TX_L1TOPO_C_U42']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U42_ADDR) )
    hardware['MINIPODS']['RX_CALO_C_U114']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U114_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U115']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U115_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U116']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U116_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U117']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U117_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U118']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U118_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U119']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U119_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U120']['TEMP'] = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U120_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U90']['TEMP']  = handle_errors( lambda: minipod_temp(Z_IIC_BUS3,MPOD_U90_ADDR))

    ######################################################
    ###################### VOLTAGES ######################
    ######################################################

    # 12V DCDV BMR458 voltages & currents
    reg_value = bmr458_mon(BMR4582_U11_ADDR,0x8B)
    hardware['POWERMODULES']['U11_12V']['VOLTAGE'] = handle_errors( lambda: 2**(-11)*(int(reg_value, 16)>>8 | (int(reg_value, 16) << 8 & 0xFF00)))
    hardware['POWERMODULES']['U11_12V']['CURRENT'] = handle_errors( lambda: lin5_11ToFloat(bmr458_mon(BMR4582_U11_ADDR,0x8C)))
    hardware['POWERMODULES']['U11_48V']['VOLTAGE'] = handle_errors( lambda: lin5_11ToFloat(bmr458_mon(BMR4582_U11_ADDR,0x88)))
    hardware['POWERMODULES']['U81']['VOLTAGE'] = handle_errors( lambda: int(ina226_reg_read(INA226_U81_ADDR,0x02,2), 16)*1.25/1000)
    hardware['POWERMODULES']['U81']['CURRENT'] = handle_errors( lambda: int(ina226_reg_read(INA226_U81_ADDR,0x01,2), 16)*2.5/1000)

    # LTM4630A power module voltages and currents
    hardware['POWERMODULES']['Z_U73_INT']['VOLTAGE']     = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U51_ADDR,0x7F,0x1F,0xAE,0xAF,1)) # read ADM1066 U51 channel VX3 INT_Z_0.85V)
    hardware['POWERMODULES']['Z_U73_INT']['CURRENT']     = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xBC))
    hardware['POWERMODULES']['Z_U59_MGTAVCC']['VOLTAGE'] = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U51_ADDR,0xFF,0x1E,0xB0,0xB1,1)) # read ADM1066 U51 channel VX4 MGTAVCC_Z_0.9V )
    hardware['POWERMODULES']['Z_U59_MGTAVCC']['CURRENT'] = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB5)   )
    hardware['POWERMODULES']['Z_U66_MGTAVTT']['VOLTAGE'] = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U51_ADDR,0x7F,0x1D,0xB2,0xB3,1)) # read ADM1066 U51 channel VX5 MGTAVTT_Z_1.2V)
    hardware['POWERMODULES']['Z_U66_MGTAVTT']['CURRENT'] = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xBD))
    hardware['POWERMODULES']['Z_U55']['VOLTAGE']           = handle_errors( lambda: 2.181 * adm1066_voltage_mon(ADM1066_U51_ADDR,0xFE,0x1F,0xA0,0xA1,1)) # read ADM1066 U51 channel VP1 2.5V)
    hardware['POWERMODULES']['Z_U55']['CURRENT']           = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xBE) )
    hardware['POWERMODULES']['Z_U59']['VOLTAGE']           = handle_errors( lambda: 4.363 * adm1066_voltage_mon(ADM1066_U51_ADDR,0xFD,0x1F,0xA2,0xA3,1)) # read ADM1066 U51 channel VP2 3.3V)
    hardware['POWERMODULES']['Z_U59']['CURRENT']           = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB7)/5 )
    hardware['POWERMODULES']['Z_U73']['VOLTAGE']           = handle_errors( lambda: 2.181 * adm1066_voltage_mon(ADM1066_U51_ADDR,0xFB,0x1F,0xA4,0xA5,1)) # read ADM1066 U51 channel VP3 1.8V)
    hardware['POWERMODULES']['Z_U73']['CURRENT']           = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB6)/2)
    hardware['POWERMODULES']['Z_U66_DDR4']['VOLTAGE']      = handle_errors( lambda: 2.0 * adm1066_voltage_mon(ADM1066_U51_ADDR,0xF7,0x1F,0xA6,0xA7,1)) # read ADM1066 U51 channel VP4 DDR4_VDDQ_0.6V)
    hardware['POWERMODULES']['Z_U66_DDR4']['CURRENT']      = handle_errors( lambda: 2*(ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xBF)/10))

    hardware['POWERMODULES']['A_U122']['VOLTAGE']        = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0xFD,0x1F,0xA2,0xA3,1)) # read ADM1066 U52 channel VP2 INT_A_0.85V)
    hardware['POWERMODULES']['A_U122']['CURRENT']        = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB0))
    hardware['POWERMODULES']['A_U77']['VOLTAGE']         = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0xFB,0x1F,0xA4,0xA5,1)) # read ADM1066 U52 channel VP3 MGTAVCC_A_0.9V)
    hardware['POWERMODULES']['A_U77']['CURRENT']         = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB8))
    hardware['POWERMODULES']['A_U30']['VOLTAGE']         = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0xF7,0x1F,0xA6,0xA7,1)) # read ADM1066 U52 channel VP4 MGTAVCC_A_1.2V)
    hardware['POWERMODULES']['A_U30']['CURRENT']         = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB1))
    hardware['POWERMODULES']['B_U123']['VOLTAGE']        = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0xDF,0x1F,0xAA,0xAB,1)) # read ADM1066 U52 channel VX1 INT_B_0.85V)
    hardware['POWERMODULES']['B_U123']['CURRENT']        = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB9))
    hardware['POWERMODULES']['B_U124']['VOLTAGE']        = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0xBF,0x1F,0xAC,0xAD,1)) # read ADM1066 U52 channel VX2 MGTAVCC_B_0.9V)
    hardware['POWERMODULES']['B_U124']['CURRENT']        = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB2))
    hardware['POWERMODULES']['B_U40']['VOLTAGE']         = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0x7F,0x1F,0xAE,0xAF,1)) # read ADM1066 U52 channel VX3 MGTAVTT_B_1.2V)
    hardware['POWERMODULES']['B_U40']['CURRENT']         = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xBA))
    hardware['POWERMODULES']['C_U126']['VOLTAGE']        = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0xFF,0x1E,0xB0,0xB1,1)) # read ADM1066 U52 channel VX4 INT_C_0.85)
    hardware['POWERMODULES']['C_U126']['CURRENT']        = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB3))
    hardware['POWERMODULES']['C_U125']['VOLTAGE']        = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U52_ADDR,0x7F,0x1D,0xB2,0xB3,1)) # read ADM1066 U52 channel VX5 MGTAVCC_C_0.9V)
    hardware['POWERMODULES']['C_U125']['CURRENT']        = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xBB))
    hardware['POWERMODULES']['C_U44']['VOLTAGE']         = handle_errors( lambda: adm1066_voltage_mon(ADM1066_U51_ADDR,0xBF,0x1F,0xAC,0xAD,1)) # read ADM1066 U51 channel VX2 MGTAVTT_C_1.2V)
    hardware['POWERMODULES']['C_U44']['CURRENT']         = handle_errors( lambda: ltc2499_current_mon(LTC2499_U1_ADDR,0x90,0xB4))

    # MiniPOD voltages
    hardware['MINIPODS']['RX_CALO_S_U91']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS2,MPOD_U91_ADDR))

    hardware['MINIPODS']['TX_FELIX_Z_U3']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS2,MPOD_U3_ADDR) )
    hardware['MINIPODS']['TX_L1TOPO_Z_U24']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS2,MPOD_U24_ADDR))
    hardware['MINIPODS']['TX_GLOBAL_Z_U56']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS2,MPOD_U56_ADDR))
    hardware['MINIPODS']['RX_FELIX_Z_U72']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS2,MPOD_U72_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_A_U32']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U32_ADDR)  )
    hardware['MINIPODS']['TX_L1TOPO_A_U25']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U25_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U96']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U96_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U102']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U102_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U103']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U103_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U104']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U104_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U105']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U105_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U106']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U106_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U107']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U107_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U27']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U27_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U97']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS5,MPOD_U97_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_B_U33']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U33_ADDR)  )
    hardware['MINIPODS']['TX_L1TOPO_B_U27']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U27_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U98']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U98_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U100']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U100_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U101']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U101_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U108']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U108_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U109']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U109_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U111']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U111_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U112']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U112_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U113']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS4,MPOD_U113_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_C_U34']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U34_ADDR) )
    hardware['MINIPODS']['TX_L1TOPO_C_U42']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U42_ADDR) )
    hardware['MINIPODS']['RX_CALO_C_U114']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U114_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U115']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U115_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U116']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U116_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U117']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U117_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U118']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U118_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U119']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U119_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U120']['VOLTAGE'] = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U120_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U90']['VOLTAGE']  = handle_errors( lambda: minipod_voltages(Z_IIC_BUS3,MPOD_U90_ADDR))

    #######################################################
    ##################### MINIPOD LOS #####################
    #######################################################

    hardware['MINIPODS']['RX_CALO_S_U91']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS2,MPOD_U91_ADDR))

    #hardware['MINIPODS']['TX_FELIX_Z_U3']['LOS']   = handle_errors( lambda: minipod_los(Z_IIC_BUS2,MPOD_U3_ADDR) )
    #hardware['MINIPODS']['TX_L1TOPO_Z_U24']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS2,MPOD_U24_ADDR))
    #hardware['MINIPODS']['TX_GLOBAL_Z_U56']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS2,MPOD_U56_ADDR))
    hardware['MINIPODS']['RX_FELIX_Z_U72']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS2,MPOD_U72_ADDR))

    #hardware['MINIPODS']['TX_L1TOPO_A_U32']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U32_ADDR)  )
    #hardware['MINIPODS']['TX_L1TOPO_A_U25']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U25_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U96']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U96_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U102']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U102_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U103']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U103_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U104']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U104_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U105']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U105_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U106']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U106_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U107']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U107_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U27']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U27_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U97']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS5,MPOD_U97_ADDR))

    #hardware['MINIPODS']['TX_L1TOPO_B_U33']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U33_ADDR)  )
    #hardware['MINIPODS']['TX_L1TOPO_B_U27']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U27_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U98']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U98_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U100']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U100_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U101']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U101_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U108']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U108_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U109']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U109_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U111']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U111_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U112']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U112_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U113']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS4,MPOD_U113_ADDR))

    #hardware['MINIPODS']['TX_L1TOPO_C_U34']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U34_ADDR) )
    #hardware['MINIPODS']['TX_L1TOPO_C_U42']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U42_ADDR) )
    hardware['MINIPODS']['RX_CALO_C_U114']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U114_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U115']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U115_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U116']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U116_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U117']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U117_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U118']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U118_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U119']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U119_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U120']['LOS'] = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U120_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U90']['LOS']  = handle_errors( lambda: minipod_los(Z_IIC_BUS3,MPOD_U90_ADDR))

    #######################################################
    ################ MINIPOD OPTICAL POWER ################
    #######################################################

    hardware['MINIPODS']['RX_CALO_S_U91']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS2,MPOD_U91_ADDR))

    hardware['MINIPODS']['TX_FELIX_Z_U3']['OPTPWR']   = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS2,MPOD_U3_ADDR) )
    hardware['MINIPODS']['TX_L1TOPO_Z_U24']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS2,MPOD_U24_ADDR))
    hardware['MINIPODS']['TX_GLOBAL_Z_U56']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS2,MPOD_U56_ADDR))
    hardware['MINIPODS']['RX_FELIX_Z_U72']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS2,MPOD_U72_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_A_U32']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U32_ADDR)  )
    hardware['MINIPODS']['TX_L1TOPO_A_U25']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U25_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U96']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U96_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U102']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U102_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U103']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U103_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U104']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U104_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U105']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U105_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U106']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U106_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U107']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U107_ADDR))
    hardware['MINIPODS']['RX_CALO_A_U27']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U27_ADDR) )
    hardware['MINIPODS']['RX_CALO_A_U97']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS5,MPOD_U97_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_B_U33']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U33_ADDR)  )
    hardware['MINIPODS']['TX_L1TOPO_B_U27']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U27_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U98']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U98_ADDR)  )
    hardware['MINIPODS']['RX_CALO_B_U100']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U100_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U101']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U101_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U108']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U108_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U109']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U109_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U111']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U111_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U112']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U112_ADDR) )
    hardware['MINIPODS']['RX_CALO_B_U113']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS4,MPOD_U113_ADDR))

    hardware['MINIPODS']['TX_L1TOPO_C_U34']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U34_ADDR) )
    hardware['MINIPODS']['TX_L1TOPO_C_U42']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U42_ADDR) )
    hardware['MINIPODS']['RX_CALO_C_U114']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U114_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U115']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U115_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U116']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U116_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U117']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U117_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U118']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U118_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U119']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U119_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U120']['OPTPWR'] = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U120_ADDR))
    hardware['MINIPODS']['RX_CALO_C_U90']['OPTPWR']  = handle_errors( lambda: minipod_opticalpower(Z_IIC_BUS3,MPOD_U90_ADDR))


    with open(i2c_file, 'w+') as f_out:
      json.dump(hardware, f_out, indent=4, sort_keys=True)


    if args.logfile != '':
      dt = datetime.datetime.now().strftime("_%d%m%Y_%H%M%S")
      with open(args.logfile + dt + '.json', 'w+') as f_out:
        json.dump(hardware, f_out, indent=4, sort_keys=True)
