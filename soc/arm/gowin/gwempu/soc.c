/*
 * Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
 * SPDX-License-Identifier: Apache-2.0
 */

#include <zephyr/device.h>
#include <zephyr/init.h>
#include <zephyr/irq.h>

/* initial ecc memory */
void z_arm_platform_init(void) {
  // register unsigned r0 __asm("r0") = DT_REG_ADDR(DT_CHOSEN(zephyr_sram));
  // register unsigned r1 __asm("r1") =
  // 	DT_REG_ADDR(DT_CHOSEN(zephyr_sram)) +
  // DT_REG_SIZE(DT_CHOSEN(zephyr_sram));

  // for (; r0 < r1; r0 += 4) {
  // 	*(unsigned int *)r0 = 0;
  // }

// #define GPIO_BASE 0x40010000
// #define GPIO_DATAOUT *(volatile unsigned short *)(GPIO_BASE + 0x0004)
// #define GPIO_OUTENSET *(volatile unsigned short *)(GPIO_BASE + 0x0010)

//   GPIO_OUTENSET |= 0x01;
//   while (1) {
//     GPIO_DATAOUT ^= 0x01;
//   }
}

static int empu_soc_init(const struct device *dev) {
  uint32_t key;

  ARG_UNUSED(dev);

  key = irq_lock();

  irq_unlock(key);

  return 0;
}

SYS_INIT(empu_soc_init, PRE_KERNEL_1, 0);
