/*
 * Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <zephyr/kernel.h>
#include <zephyr/sys/printk.h>

#define GPIO_BASE 0x40010000
#define GPIO_DATAOUT *(volatile unsigned short *)(GPIO_BASE + 0x0004)
#define GPIO_OUTENSET *(volatile unsigned short *)(GPIO_BASE + 0x0010)

void main() {
  GPIO_OUTENSET |= 0x01;

  while (1) {
    GPIO_DATAOUT ^= 0x01;

    k_msleep(1000);
  }
}
