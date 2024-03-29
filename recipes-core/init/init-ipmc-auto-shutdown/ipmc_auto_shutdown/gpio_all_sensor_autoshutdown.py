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

from __future__ import print_function
from math import trunc
from time import sleep
import os, logging, json, time
import subprocess
from periphery import I2C

def check_temp(temp_sensor):
    raw = subprocess.getoutput("cat /sys/bus/iio/devices/iio\:device0/in_" + temp_sensor + "_temp_raw")
    raw_value = float(raw)

    offset = subprocess.getoutput("cat /sys/bus/iio/devices/iio\:device0/in_" + temp_sensor + "_temp_offset")
    offset_value = float(offset)

    scale = subprocess.getoutput("cat /sys/bus/iio/devices/iio\:device0/in_" + temp_sensor + "_temp_scale")
    scale_value = float(scale)

    temp = (raw_value + offset_value)*scale_value/1024
    return temp

#Get and Check Temperature threshold
TEMP_THRESHOLD = float(95)
if (TEMP_THRESHOLD > 95.0):
    print("Temperature Threshold set to %.2f. This is too high!!" %TEMP_THRESHOLD)
    TEMP_THRESHOLD = float(95)
    print("Starting temperature monitoring. Automatic shutdown will occur at 95 C")

elif (TEMP_THRESHOLD < 65.0):
    print("Temperature Threshold set to %.2f. This is too low!!" %TEMP_THRESHOLD)
    TEMP_THRESHOLD = float(65)
    print("Starting temperature monitoring. Automatic shutdown will occur at 65 C")

else:
    print("Starting temperature monitoring. Automatic shutdown will occur at %.2f C." %TEMP_THRESHOLD)

logging.basicConfig(filename='/logs/temp.log', level=logging.INFO)

#Wait for first round of 12c polling to complete and be written to file 
i2c_file = '/logs/i2c_hardware.json'
while not os.path.isfile(i2c_file):
   sleep(10)

gfex_temp = float(30)
while gfex_temp < TEMP_THRESHOLD:

    logging.info(str(subprocess.getoutput("date")))

    try:
        with open(i2c_file, 'r') as j :
            i2c_data = json.load(j)
    except:
        logging.info("Problems opening the json file. Trying again.")
        continue

    temp = i2c_data['TEMPSENSORS']
    pwr = i2c_data['POWERMODULES']
    mpd = i2c_data['MINIPODS']
    
    # Read board temperature sensors zynq
    temp_ps = check_temp("temp0_ps")
    temp_pl = check_temp("temp2_pl")
    temp_zynq = max(temp_ps, temp_pl)
    logging.info("ZYNQ+ temperature is %.2f C." %temp_zynq)

    # Read board temperature sensors AD7414
    temp_7414_A=temp['U82']['TEMP']
    temp_7414_B=temp['U83']['TEMP']
    temp_7414_C=temp['U84']['TEMP']
    temp_7414_D=temp['U87']['TEMP']
    temp_ad7414 = max(temp_7414_A,temp_7414_B,temp_7414_C,temp_7414_D)
    logging.info("AD7414 temperature is %.2f C." %temp_ad7414)

    #12V power module BMR458 temperature
    temp_bmr458=pwr['U11_12V']['TEMP']
    logging.info("BMR458 temperature is %.2f C." %temp_bmr458)

    # LTM4630A power modules Temperature monitoring
    temp_ltm_A=pwr['Z_U66_MGTAVTT']['TEMP']
    temp_ltm_B=pwr['Z_U73']['TEMP']
    temp_ltm_C=pwr['Z_U55']['TEMP']
    temp_ltm_D=pwr['Z_U59']['TEMP']
    temp_ltm_E=pwr['A_U122']['TEMP']
    temp_ltm_F=pwr['A_U77']['TEMP']
    temp_ltm_G=pwr['A_U30']['TEMP']
    temp_ltm_H=pwr['B_U123']['TEMP']
    temp_ltm_I=pwr['B_U124']['TEMP']
    temp_ltm_J=pwr['B_U40']['TEMP']
    temp_ltm_K=pwr['C_U126']['TEMP']
    temp_ltm_L=pwr['C_U125']['TEMP']
    temp_ltm_M=pwr['C_U44']['TEMP']
    temp_DCDC = max(temp_bmr458,temp_ltm_A,temp_ltm_B,temp_ltm_C,temp_ltm_D,temp_ltm_E,temp_ltm_F,temp_ltm_G,temp_ltm_H,temp_ltm_I,temp_ltm_J,temp_ltm_K,temp_ltm_L,temp_ltm_M)
    logging.info("gfex DCDC temperature is %.2f C." %temp_DCDC)

    # MiniPODs monitoring
    temp_mini_spare=mpd['RX_CALO_S_U91']['TEMP']

    temp_mini_ZA=mpd['TX_FELIX_Z_U3']['TEMP']
    temp_mini_ZB=mpd['TX_L1TOPO_Z_U24']['TEMP']
    temp_mini_ZC=mpd['TX_GLOBAL_Z_U56']['TEMP']
    temp_mini_ZD=mpd['RX_FELIX_Z_U72']['TEMP']
    
    temp_mini_A1=mpd['TX_L1TOPO_A_U32']['TEMP']
    temp_mini_A2=mpd['TX_L1TOPO_A_U25']['TEMP']
    temp_mini_A3=mpd['RX_CALO_A_U96']['TEMP']
    temp_mini_A4=mpd['RX_CALO_A_U102']['TEMP']
    temp_mini_A5=mpd['RX_CALO_A_U103']['TEMP']
    temp_mini_A6=mpd['RX_CALO_A_U104']['TEMP']
    temp_mini_A7=mpd['RX_CALO_A_U105']['TEMP']
    temp_mini_A8=mpd['RX_CALO_A_U106']['TEMP']
    temp_mini_A9=mpd['RX_CALO_A_U27']['TEMP']
    temp_mini_A10=mpd['RX_CALO_A_U97']['TEMP']

    temp_mini_B1=mpd['TX_L1TOPO_B_U33']['TEMP']
    temp_mini_B2=mpd['TX_L1TOPO_B_U27']['TEMP']
    temp_mini_B3=mpd['RX_CALO_B_U98']['TEMP']
    temp_mini_B4=mpd['RX_CALO_B_U100']['TEMP']
    temp_mini_B5=mpd['RX_CALO_B_U101']['TEMP']
    temp_mini_B6=mpd['RX_CALO_B_U108']['TEMP']
    temp_mini_B7=mpd['RX_CALO_B_U109']['TEMP']
    temp_mini_B8=mpd['RX_CALO_B_U111']['TEMP']
    temp_mini_B9=mpd['RX_CALO_B_U112']['TEMP']
    temp_mini_B10=mpd['RX_CALO_B_U113']['TEMP']

    temp_mini_C1=mpd['TX_L1TOPO_C_U34']['TEMP']
    temp_mini_C2=mpd['TX_L1TOPO_C_U42']['TEMP']
    temp_mini_C3=mpd['RX_CALO_C_U114']['TEMP']
    temp_mini_C4=mpd['RX_CALO_C_U115']['TEMP']
    temp_mini_C5=mpd['RX_CALO_C_U116']['TEMP']
    temp_mini_C6=mpd['RX_CALO_C_U117']['TEMP']
    temp_mini_C7=mpd['RX_CALO_C_U118']['TEMP']
    temp_mini_C8=mpd['RX_CALO_C_U119']['TEMP']
    temp_mini_C9=mpd['RX_CALO_C_U120']['TEMP']
    temp_mini_C10=mpd['RX_CALO_C_U90']['TEMP']


    temp_mini_z = max(temp_mini_ZA,temp_mini_ZB,temp_mini_ZC,temp_mini_ZD, temp_mini_spare)
    temp_mini_a = max(temp_mini_A1,temp_mini_A2,temp_mini_A3,temp_mini_A4,temp_mini_A5,temp_mini_A6,temp_mini_A7,temp_mini_A8,temp_mini_A9,temp_mini_A10)
    temp_mini_b = max(temp_mini_B1,temp_mini_B2,temp_mini_B3,temp_mini_B4,temp_mini_B5,temp_mini_B6,temp_mini_B7,temp_mini_B8,temp_mini_B9,temp_mini_B10)
    temp_mini_c = max(temp_mini_C1,temp_mini_C2,temp_mini_C3,temp_mini_C4,temp_mini_C5,temp_mini_C6,temp_mini_C7,temp_mini_C8,temp_mini_C9,temp_mini_C10)

    temp_minipods = max(temp_mini_z,temp_mini_a,temp_mini_b,temp_mini_c)
    logging.info("Minipods temperature is %.2f C." %temp_minipods)

    gfex_temp = max(temp_zynq,temp_ad7414,temp_DCDC,temp_minipods)
    logging.info("gFEX_Max temperature is %.2f C." %gfex_temp)
    
    temp_datagpio= int(temp_zynq)+int(temp_ad7414)*256+int(temp_DCDC)*256*256+int(temp_minipods)*256*256*256
    temp_datagpiostr= str(temp_datagpio)

    gpiocmdA = subprocess.getoutput("/software/misc/ipmc_auto_shutdown/gpio-dev-mem-test -g 0x00A0020000 -o" + temp_datagpiostr)
    gpiocmdB = subprocess.getoutput("/software/misc/ipmc_auto_shutdown/gpio-dev-mem-test -g 0x00A0022000 -o 1" )
    gpiocmdC = subprocess.getoutput("/software/misc/ipmc_auto_shutdown/gpio-dev-mem-test -g 0x00A0022000 -o 0" )
    gpiocmdD = subprocess.getoutput("/software/misc/ipmc_auto_shutdown/gpio-dev-mem-test -g 0x00A0021000 -i" )
    
    time.sleep(1)

else:
    logging.info("The gFEX temp is %.2f C and has overheated. Shutting down the board immediately." % gfex_temp)
    print("The gFEX temp is %.2f C and has overheated. Shutting down the board immediately." % gfex_temp)
    set_i2c_mux(TCA9548_U93_ADDR,SENSOR_IIC_BUS)
    time.sleep(10)
    adm1066_shutdown(ADM1066_U52_ADDR)
