#!/usr/bin/env python3
from __future__ import print_function
from math import trunc
from time import sleep
import sys

from periphery import I2C

TCA9548_U93_ADDR = 0x70 #1110_000X  0XE0
Z_IIC_BUS2       = 0x02
Z_IIC_BUS3       = 0x04
Z_IIC_BUS4       = 0x08
Z_IIC_BUS5       = 0x10

def minipod_reg_wr(i2c_bus_addr,dev_addr,page_addr,reg_addr,reg_value):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(TCA9548_U93_ADDR, [I2C.Message([i2c_bus_addr])]) # select i2c bus
  i2c.transfer(dev_addr, [I2C.Message([127,page_addr])])        # set the page
  i2c.transfer(dev_addr, [I2C.Message([reg_addr,reg_value])])   # write the value to the reg_addr
  i2c.close()

def minipod_reg_rd(i2c_bus_addr,dev_addr,page_addr,reg_addr):
  i2c = I2C("/dev/i2c-1")
  i2c.transfer(TCA9548_U93_ADDR, [I2C.Message([i2c_bus_addr])]) # select i2c bus

  read = I2C.Message([0x0], read=True)
  i2c.transfer(dev_addr, [I2C.Message([127,page_addr])]) # set the page
  i2c.transfer(dev_addr, [I2C.Message([reg_addr])])      # set reg_addr
  i2c.transfer(dev_addr, [read])
  i2c.close()

  return read.data[0]

def minipod_temp(i2c_bus_addr,dev_addr):
  temperature=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,28) #temperature
  return temperature

def minipod_los(i2c_bus_addr,dev_addr):
  los_h=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,9); #LOS channel 8-11
  los_l=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,10); #LOS channel 0-7
  los = los_h*256 + los_l
  return los

def minipod_voltages(i2c_bus_addr, dev_addr):
  vh=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,32) #3.3V monitoring VH
  vl=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,33) #3.3V monitoring VL
  voltage1 = (vh*256 + vl)*0.0001
  vh=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,34) #2.5V monitoring VH
  vl=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,35) #2.5V monitoring VL
  voltage2 = (vh*256 + vl)*0.0001
  return [voltage1, voltage2]

def minipod_opticalpower(i2c_bus_addr, dev_addr):
  optpwr = []
  for i in range (0, 24, 2):
    oph=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,64+i) 
    opl=minipod_reg_rd(i2c_bus_addr,dev_addr,0x00,65+i) 
    optpwr.insert(0, (oph*256 + opl)*0.0000001)
    
  return optpwr
