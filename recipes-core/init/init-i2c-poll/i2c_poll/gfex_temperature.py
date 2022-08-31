#!/usr/bin/env python3
from __future__ import print_function
from math import trunc
from time import sleep
import sys
import codecs

from periphery import I2C

TCA9548_U93_ADDR = 0x70 #1110_000X  0XE0
SENSOR_IIC_BUS   = 0x01

def set_i2c_mux(dev_addr,channel):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(dev_addr, [I2C.Message([channel])])
  i2c.close()

def ad7414_reg_write(dev_addr,reg_addr,reg_value):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(dev_addr, [I2C.Message([reg_addr, reg_value])]) # SENSOR_IIC_BUS is selected
  i2c.close()

def ad7414_reg_read(dev_addr,reg_addr,nbits):
  i2c = I2C("/dev/i2c-1")
  read = I2C.Message([0x0]*nbits, read=True)
  i2c.transfer(dev_addr, [I2C.Message([reg_addr]), read]) # reg for read
  i2c.close()
  return codecs.encode(bytes(bytearray(read.data)), 'hex')

def ad7414_mon(dev_addr):
  #set the I2C Mux to SENSOR_IIC_BUS 0x01
  set_i2c_mux(TCA9548_U93_ADDR,SENSOR_IIC_BUS)

  ad7414_reg_write(dev_addr,0x1,0x48) #alert active low
  ad7414_reg_write(dev_addr,0x2,0x3F) #up limit is 63 Degree
  ad7414_reg_write(dev_addr,0x3,0x80) #low limit is 0 degree
  temperature=ad7414_reg_read(dev_addr,0x0,1)#read the temperature value
  return int(temperature,16)

def ltc2499_temp_mon(dev_addr,reg_addr0,reg_addr1):
  #set the I2C Mux to SENSOR_IIC_BUS 0x01 PUT INSIDE METHOD
  set_i2c_mux(TCA9548_U93_ADDR,SENSOR_IIC_BUS)

  # two delays as the chip is slow
  sleep(0.5)
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(dev_addr, [I2C.Message([reg_addr1,reg_addr0])])# Reg for read
  sleep(0.5)

  read = I2C.Message([0x0]*4, read=True)
  i2c.transfer(dev_addr, [read])
  i2c.close()
  adc_code=int(codecs.encode(bytes(bytearray(read.data)),'hex'), 16)

  resolution=2500./0x80000000
  amplitude=(adc_code-0x40000000)*resolution
  if(adc_code==0x3FFFFFFF): amplitude=-1

  temperature= 326-0.5*amplitude
  return temperature
