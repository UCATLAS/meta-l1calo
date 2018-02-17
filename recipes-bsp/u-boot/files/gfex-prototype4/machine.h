/*
 * Configuration for Xilinx ZynqMP zcu102
 *
 * (C) Copyright 2015 Xilinx, Inc.
 * Michal Simek <michal.simek@xilinx.com>
 *
 * SPDX-License-Identifier:	GPL-2.0+
 */

#ifndef __CONFIG_GFEX_V4_H
#define __CONFIG_GFEX_V4_H

#define CONFIG_ZYNQ_I2C0
#define CONFIG_ZYNQ_I2C1
#define CONFIG_SYS_I2C_MAX_HOPS		1
#define CONFIG_SYS_NUM_I2C_BUSES	1
#define CONFIG_SYS_I2C_BUSES	{ \
				{0, {I2C_NULL_HOP} }, \
				}

#define CONFIG_SYS_I2C_ZYNQ
#define CONFIG_PCA953X
#define CONFIG_CMD_PCA953X
#define CONFIG_CMD_PCA953X_INFO

#define CONFIG_SYS_I2C_EEPROM_ADDR_LEN	1
#define CONFIG_CMD_EEPROM
#define CONFIG_ZYNQ_EEPROM_BUS		5
#define CONFIG_ZYNQ_GEM_EEPROM_ADDR	0x54
#define CONFIG_ZYNQ_GEM_I2C_MAC_OFFSET	0x20

#include <configs/xilinx_zynqmp.h>

#endif /* __CONFIG_GFEX_V4_H */
