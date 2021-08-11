#!/usr/bin/env python3
from __future__ import print_function
from math import trunc
from time import sleep
import sys
import codecs
from periphery import I2C
TCA9548_U93_ADDR = 0x70 #1110_000X  0XE0
SENSOR_IIC_BUS   = 0x01

# imported from manufacturer tech reference manual for the chip (ask Shaochun where it came from)
#convert a LinearFloat5_11 formatted word into a floating point value
def lin5_11ToFloat(wordValue):
  binValue = int(wordValue,16)
  binValue=binValue>>8 | (binValue << 8 & 0xFF00)
  #print('{0:s}' binValue)

  #wordValue = ' '.join(format(x, 'b') for x in bytearray(wordValue))
  exponent = binValue>>11      #extract exponent as MS 5 bits
  mantissa = binValue & 0x7ff  #extract mantissa as LS 11 bits

  #sign extended exponent
  if exponent > 0x0F: exponent |= 0xE0
  if exponent > 127: exponent -= 2**8
  #sign extended mantissa
  if mantissa > 0x03FF: mantissa |= 0xF800
  if mantissa > 32768: mantissa -= 2**16
  # compute value as
  return mantissa * (2**exponent)

def bmr458_mon(dev_addr,reg_addr):
  #set the i2c mux channel, since all the device is under SENSOR_IIC_BUS, just need to set once at the begining.
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(TCA9548_U93_ADDR, [I2C.Message([SENSOR_IIC_BUS])]) # SENSOR_IIC_BUS is selected
  i2c.close()

  i2c = I2C("/dev/i2c-1")
  write = I2C.Message([reg_addr])
  read = I2C.Message([0x0]*2, read=True)
  i2c.transfer(dev_addr, [write, read])
  i2c.close()
  return codecs.encode(bytes(bytearray(read.data)), 'hex')

def ina226_reg_write(dev_addr,reg_addr,reg_value0,reg_value1):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(dev_addr, [I2C.Message([reg_addr,reg_value0,reg_value1])])
  i2c.close()

def ina226_reg_read(dev_addr,reg_addr,nbits):
  i2c = I2C("/dev/i2c-1")
  read = I2C.Message([0x0]*nbits, read=True)
  i2c.transfer(dev_addr, [I2C.Message([reg_addr]),read])
  i2c.close()
  return codecs.encode(bytes(bytearray(read.data)), 'hex')

def ltc2499_current_mon(dev_addr,reg_addr0,reg_addr1):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(dev_addr, [I2C.Message([reg_addr1, reg_addr0])])
  sleep(0.5)
  read = I2C.Message([0x0]*4, read=True)
  i2c.transfer(dev_addr, [read])
  adc_code=codecs.encode(bytes(bytearray(read.data)), 'hex')
  i2c.close()
  resolution=2500./0x80000000
  if(int(adc_code,16)<0x40000000):
    amplitude=0
  else:
    amplitude=(int(adc_code,16)-0x40000000)*resolution
  return amplitude/40

def adm1066_voltage_mon(dev_addr,reg_value_80,reg_value_81,reg_addr_vh,reg_addr_vl,average_on):
  #set the i2c mux channel, since all the device is under SENSOR_IIC_BUS, just need to set once at the begining.
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(TCA9548_U93_ADDR, [I2C.Message([SENSOR_IIC_BUS])]) # SENSOR_IIC_BUS is selected
  i2c.close()
  
  i2c = I2C("/dev/i2c-1")
  #read the ADM1066 U52 with addr 0x34 and 0x35
  read = I2C.Message([0x0], read=True)
  i2c.transfer(dev_addr, [I2C.Message([0x82,0x0+average_on*4])]) # reset STOPWRITE BIT
  i2c.transfer(dev_addr, [I2C.Message([0x80,reg_value_80])])     # reg 0x80 select channel
  i2c.transfer(dev_addr, [I2C.Message([0x81,reg_value_81])])     # reg 0x81
  i2c.transfer(dev_addr, [I2C.Message([0x82,0x01+average_on*4])])# reg 0x82 select go bit
  i2c.transfer(dev_addr, [I2C.Message([0x82])])                  # reg 0x82

  while True:
    sleep(1.0)
    i2c.transfer(dev_addr, [read])
    status=read.data[0] #keep checking the go bit, until it is equal average_on*4
    if status == average_on*4: break

  i2c.transfer(dev_addr, [I2C.Message([0x82,0x08+average_on*4])])
  i2c.transfer(dev_addr, [I2C.Message([reg_addr_vh]), # reg 0xA8 VH high 8bit voltage value
                          read
                          ])
  vh=read.data[0]

  i2c.transfer(dev_addr, [I2C.Message([reg_addr_vl]), # reg 0xA9 VH low 8 bits voltag value
                          read
                          ])
  vl=read.data[0]
  i2c.close()

  voltage= ((vh*256 + vl)*2.048/4095)/(2**(average_on*4))
  return voltage

