/*
 * Copyright (c) 2016 Intel Corporation
 *
 * SPDX-License-Identifier: Apache-2.0
 */

#include <zephyr/kernel.h>
#include <zephyr/drivers/gpio.h>
#include <zephyr/drivers/i2c.h>

/* 1000 msec = 1 sec */
#define SLEEP_TIME_MS 100

/* The devicetree node identifier for the "led0" alias. */
#define LED0_NODE DT_ALIAS(led0)
// #define IIC_NODE  DT_NODELABEL(i2c0)

/*
 * A build error on this line means your board is unsupported.
 * See the sample documentation for information on how to fix this.
 */
static const struct gpio_dt_spec led = GPIO_DT_SPEC_GET(LED0_NODE, gpios);
// static const struct i2c_dt_spec i2c = I2C_DT_SPEC_GET(IIC_NODE);

#define iic_wait() k_busy_wait(1)

#define GPIO_BASE     0x40010000
#define GPIO_DATAOUT  *(volatile unsigned short *)(GPIO_BASE + 0x0004)
#define GPIO_OUTENSET *(volatile unsigned short *)(GPIO_BASE + 0x0010)

#define IIC_BIAS      1
#define IIC_GPIO(val) GPIO_DATAOUT = (GPIO_DATAOUT & (~(0b11 << IIC_BIAS))) | ((val) << IIC_BIAS)

void iic_init(uint32_t rate)
{
	GPIO_OUTENSET |= 0b11 << IIC_BIAS;
	iic_wait();
	IIC_GPIO(0b11);
	iic_wait();

	return;
}

void sccb_start()
{
	IIC_GPIO(0b11);
	iic_wait();
	IIC_GPIO(0b10);
	iic_wait();
	IIC_GPIO(0b00);
	iic_wait();
}

void sccb_data(uint8_t data)
{
	uint8_t val = 0;
	for (size_t i = 0; i < 8; i++) {
		val = ((data << i) & 0x80) ? 1 : 0;
		IIC_GPIO(0b00 | val);
		iic_wait();
		IIC_GPIO(0b10 | val);
		iic_wait();
		IIC_GPIO(0b00 | val);
		iic_wait();
	}

	IIC_GPIO(0b11);
	iic_wait();
	IIC_GPIO(0b01);
	iic_wait();
}

void sccb_stop()
{
	IIC_GPIO(0b00);
	iic_wait();
	IIC_GPIO(0b10);
	iic_wait();
	IIC_GPIO(0b11);
	iic_wait();
}

void iic_set(uint8_t addr, uint8_t reg, uint8_t data)
{
	sccb_start();
	sccb_data(addr << 1 | 0);
	sccb_data(reg);
	sccb_data(data);
	sccb_stop();

	k_msleep(3);
}

void ov_set(uint8_t reg, uint8_t data)
{
	iic_set(0x60 >> 1, reg, data);
}

void ov_init()
{

	ov_set(0xFF, 0x01);
	ov_set(0x12, 0x80);
	ov_set(0xFF, 0x00);
	ov_set(0x2c, 0xff);
	ov_set(0x2e, 0xdf);
	ov_set(0xFF, 0x01);
	ov_set(0x3c, 0x32);
	ov_set(0x11, 0x80);
	ov_set(0x09, 0x02);
	ov_set(0x04, 0x28);
	ov_set(0x13, 0xE5);
	ov_set(0x14, 0x48);
	ov_set(0x15, 0x00);
	ov_set(0x2c, 0x0c);
	ov_set(0x33, 0x78);
	ov_set(0x3a, 0x33);
	ov_set(0x3b, 0xfb);
	ov_set(0x3e, 0x00);
	ov_set(0x43, 0x11);
	ov_set(0x16, 0x10);
	ov_set(0x39, 0x02);
	ov_set(0x35, 0x88);
	ov_set(0x22, 0x0a);
	ov_set(0x37, 0x40);
	ov_set(0x23, 0x00);
	ov_set(0x34, 0xa0);
	ov_set(0x06, 0x02);
	ov_set(0x06, 0x88);
	ov_set(0x07, 0xc0);
	ov_set(0x0d, 0xb7);
	ov_set(0x0e, 0x01);
	ov_set(0x4c, 0x00);
	ov_set(0x4a, 0x81);
	ov_set(0x21, 0x99);
	ov_set(0x24, 0x40);
	ov_set(0x25, 0x38);
	ov_set(0x26, 0x82);
	ov_set(0x48, 0x00);
	ov_set(0x49, 0x00);
	ov_set(0x5c, 0x00);
	ov_set(0x63, 0x00);
	ov_set(0x46, 0x00);
	ov_set(0x47, 0x00);
	ov_set(0x0C, 0x3A);
	ov_set(0x5D, 0x55);
	ov_set(0x5E, 0x7d);
	ov_set(0x5F, 0x7d);
	ov_set(0x60, 0x55);
	ov_set(0x61, 0x70);
	ov_set(0x62, 0x80);
	ov_set(0x7c, 0x05);
	ov_set(0x20, 0x80);
	ov_set(0x28, 0x30);
	ov_set(0x6c, 0x00);
	ov_set(0x6d, 0x80);
	ov_set(0x6e, 0x00);
	ov_set(0x70, 0x02);
	ov_set(0x71, 0x94);
	ov_set(0x73, 0xc1);
	ov_set(0x3d, 0x34);
	ov_set(0x5a, 0x57);
	ov_set(0x4F, 0xbb);
	ov_set(0x50, 0x9c);
	ov_set(0xFF, 0x00);
	ov_set(0xe5, 0x7f);
	ov_set(0xF9, 0xC0);
	ov_set(0x41, 0x24);
	ov_set(0xE0, 0x14);
	ov_set(0x76, 0xff);
	ov_set(0x33, 0xa0);
	ov_set(0x42, 0x20);
	ov_set(0x43, 0x18);
	ov_set(0x4c, 0x00);
	ov_set(0x87, 0xD0);
	ov_set(0x88, 0x3f);
	ov_set(0xd7, 0x03);
	ov_set(0xd9, 0x10);
	ov_set(0xD3, 0x82);
	ov_set(0xc8, 0x08);
	ov_set(0xc9, 0x80);
	ov_set(0x7C, 0x00);
	ov_set(0x7D, 0x00);
	ov_set(0x7C, 0x03);
	ov_set(0x7D, 0x48);
	ov_set(0x7D, 0x48);
	ov_set(0x7C, 0x08);
	ov_set(0x7D, 0x20);
	ov_set(0x7D, 0x10);
	ov_set(0x7D, 0x0e);
	ov_set(0x90, 0x00);
	ov_set(0x91, 0x0e);
	ov_set(0x91, 0x1a);
	ov_set(0x91, 0x31);
	ov_set(0x91, 0x5a);
	ov_set(0x91, 0x69);
	ov_set(0x91, 0x75);
	ov_set(0x91, 0x7e);
	ov_set(0x91, 0x88);
	ov_set(0x91, 0x8f);
	ov_set(0x91, 0x96);
	ov_set(0x91, 0xa3);
	ov_set(0x91, 0xaf);
	ov_set(0x91, 0xc4);
	ov_set(0x91, 0xd7);
	ov_set(0x91, 0xe8);
	ov_set(0x91, 0x20);
	ov_set(0x92, 0x00);
	ov_set(0x93, 0x06);
	ov_set(0x93, 0xe3);
	ov_set(0x93, 0x03);
	ov_set(0x93, 0x03);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x02);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x00);
	ov_set(0x93, 0x00);
	ov_set(0x96, 0x00);
	ov_set(0x97, 0x08);
	ov_set(0x97, 0x19);
	ov_set(0x97, 0x02);
	ov_set(0x97, 0x0c);
	ov_set(0x97, 0x24);
	ov_set(0x97, 0x30);
	ov_set(0x97, 0x28);
	ov_set(0x97, 0x26);
	ov_set(0x97, 0x02);
	ov_set(0x97, 0x98);
	ov_set(0x97, 0x80);
	ov_set(0x97, 0x00);
	ov_set(0x97, 0x00);
	ov_set(0xa4, 0x00);
	ov_set(0xa8, 0x00);
	ov_set(0xc5, 0x11);
	ov_set(0xc6, 0x51);
	ov_set(0xbf, 0x80);
	ov_set(0xc7, 0x10);
	ov_set(0xb6, 0x66);
	ov_set(0xb8, 0xA5);
	ov_set(0xb7, 0x64);
	ov_set(0xb9, 0x7C);
	ov_set(0xb3, 0xaf);
	ov_set(0xb4, 0x97);
	ov_set(0xb5, 0xFF);
	ov_set(0xb0, 0xC5);
	ov_set(0xb1, 0x94);
	ov_set(0xb2, 0x0f);
	ov_set(0xc4, 0x5c);
	ov_set(0xa6, 0x00);
	ov_set(0xa7, 0x20);
	ov_set(0xa7, 0xd8);
	ov_set(0xa7, 0x1b);
	ov_set(0xa7, 0x31);
	ov_set(0xa7, 0x00);
	ov_set(0xa7, 0x18);
	ov_set(0xa7, 0x20);
	ov_set(0xa7, 0xd8);
	ov_set(0xa7, 0x19);
	ov_set(0xa7, 0x31);
	ov_set(0xa7, 0x00);
	ov_set(0xa7, 0x18);
	ov_set(0xa7, 0x20);
	ov_set(0xa7, 0xd8);
	ov_set(0xa7, 0x19);
	ov_set(0xa7, 0x31);
	ov_set(0xa7, 0x00);
	ov_set(0xa7, 0x18);
	ov_set(0x7f, 0x00);
	ov_set(0xe5, 0x1f);
	ov_set(0xe1, 0x77);
	ov_set(0xdd, 0x7f);
	ov_set(0xC2, 0x0E);
	ov_set(0xFF, 0x01);
	ov_set(0xFF, 0x00);
	ov_set(0xE0, 0x04);
	ov_set(0xDA, 0x04);
	ov_set(0xD7, 0x03);
	ov_set(0xE1, 0x77);
	ov_set(0xE0, 0x00);
	ov_set(0xFF, 0x00);
	ov_set(0x05, 0x01);
	ov_set(0x5A, 0xA0);
	ov_set(0x5B, 0x78);
	ov_set(0x5C, 0x00);
	ov_set(0xFF, 0x01);
	ov_set(0x11, 0x80);
	ov_set(0xFF, 0x01);
	ov_set(0x12, 0x40);
	ov_set(0x03, 0x0A);
	ov_set(0x32, 0x09);
	ov_set(0x17, 0x11);
	ov_set(0x18, 0x43);
	ov_set(0x19, 0x00);
	ov_set(0x1A, 0x4b);
	ov_set(0x3d, 0x38);
	ov_set(0x35, 0xda);
	ov_set(0x22, 0x1a);
	ov_set(0x37, 0xc3);
	ov_set(0x34, 0xc0);
	ov_set(0x06, 0x88);
	ov_set(0x0d, 0x87);
	ov_set(0x0e, 0x41);
	ov_set(0x42, 0x03);
	ov_set(0xFF, 0x00);
	ov_set(0x05, 0x01);
	ov_set(0xE0, 0x04);
	ov_set(0xC0, 0x64);
	ov_set(0xC1, 0x4B);
	ov_set(0x8C, 0x00);
	ov_set(0x53, 0x00);
	ov_set(0x54, 0x00);
	ov_set(0x51, 0xC8);
	ov_set(0x52, 0x96);
	ov_set(0x55, 0x00);
	ov_set(0x57, 0x00);
	ov_set(0x86, 0x3D);
	ov_set(0x50, 0x80);
	ov_set(0xD3, 0x80);
	ov_set(0x05, 0x00);
	ov_set(0xE0, 0x00);
	ov_set(0xFF, 0x00);
	ov_set(0x05, 0x00);
	ov_set(0xFF, 0x00);
	ov_set(0xE0, 0x04);
	ov_set(0xDA, 0x04);
	ov_set(0xD7, 0x03);
	ov_set(0xE1, 0x77);
	ov_set(0xE0, 0x00);

	return;
}

void main(void)
{
	int ret;

	if (!gpio_is_ready_dt(&led)) {
		return;
	}

	ret = gpio_pin_configure_dt(&led, GPIO_OUTPUT_ACTIVE);
	if (ret < 0) {
		return;
	}
	// GPIO_OUTENSET |= 0x1 << 0;

	iic_init(100);
	k_msleep(10*SLEEP_TIME_MS);
	ov_init();

	while (1) {
		ret = gpio_pin_toggle_dt(&led);
		// IIC_GPIO ^= 0x1;
		// GPIO_DATAOUT ^= 0x5;
		if (ret < 0) {
			return;
		}
		k_msleep(SLEEP_TIME_MS);
	}
}