/*
 * Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */
#define DT_DRV_COMPAT gowin_empu_gpio

#include <zephyr/drivers/gpio.h>
#include <zephyr/drivers/gpio/gpio_utils.h>

struct gpio_gowin_empu_reg {
	volatile uint32_t DATA;
	volatile uint32_t DATAOUT;
	volatile uint32_t reserved[2];
	volatile uint32_t OUTENSET;
	volatile uint32_t OUTENCLR;
} __packed;

struct gpio_gowin_empu_config {
	struct gpio_driver_config common;
	struct gpio_gowin_empu_reg *reg;
};

struct gpio_gowin_empu_data {
	struct gpio_driver_data common;
	sys_slist_t callbacks;
};

static int gowin_empu_gpio_pin_configure(const struct device *port, gpio_pin_t pin,
					 gpio_flags_t flags)
{
	const struct gpio_gowin_empu_config *cfg = (struct gpio_gowin_empu_config *)port->config;

	if ((flags & GPIO_OUTPUT) != 0U) {
		cfg->reg->OUTENSET |= BIT(pin);
	} else {
		cfg->reg->OUTENCLR |= BIT(pin);
	}
	return 0;
}

static int gowin_empu_gpio_port_get_raw(const struct device *port, gpio_port_value_t *value)
{
	const struct gpio_gowin_empu_config *cfg = (struct gpio_gowin_empu_config *)port->config;
	*value = cfg->reg->DATA;
	return 0;
}

static int gowin_empu_gpio_port_set_masked_raw(const struct device *port, gpio_port_pins_t mask,
					       gpio_port_value_t value)
{
	const struct gpio_gowin_empu_config *cfg = (struct gpio_gowin_empu_config *)port->config;
	cfg->reg->DATAOUT = (cfg->reg->DATAOUT & ~mask) | (value & mask);
	return 0;
}

static int gowin_empu_gpio_port_set_bits_raw(const struct device *port, gpio_port_pins_t pins)
{
	const struct gpio_gowin_empu_config *cfg = (struct gpio_gowin_empu_config *)port->config;
	cfg->reg->DATAOUT |= pins;
	return 0;
}

static int gowin_empu_gpio_port_clear_bits_raw(const struct device *port, gpio_port_pins_t pins)
{
	const struct gpio_gowin_empu_config *cfg = (struct gpio_gowin_empu_config *)port->config;
	cfg->reg->DATAOUT &= ~pins;
	return 0;
}

static int gowin_empu_gpio_port_toggle_bits(const struct device *port, gpio_port_pins_t pins)
{
	const struct gpio_gowin_empu_config *cfg = (struct gpio_gowin_empu_config *)port->config;
	cfg->reg->DATAOUT ^= pins;
	return 0;
}

static int gowin_empu_gpio_pin_interrupt_configure(const struct device *port, gpio_pin_t pin,
						   enum gpio_int_mode mode, enum gpio_int_trig trig)
{
	return -ENOTSUP;
}

static int gowin_empu_gpio_manage_callback(const struct device *port, struct gpio_callback *cb,
					   bool set)
{
	return -ENOTSUP;
}

static uint32_t gowin_empu_gpio_get_pending_int(const struct device *dev)
{
	return 0;
}

static const struct gpio_driver_api gowin_empu_gpio_api = {
	.pin_configure = gowin_empu_gpio_pin_configure,
	.port_get_raw = gowin_empu_gpio_port_get_raw,
	.port_set_masked_raw = gowin_empu_gpio_port_set_masked_raw,
	.port_set_bits_raw = gowin_empu_gpio_port_set_bits_raw,
	.port_clear_bits_raw = gowin_empu_gpio_port_clear_bits_raw,
	.port_toggle_bits = gowin_empu_gpio_port_toggle_bits,
	.pin_interrupt_configure = gowin_empu_gpio_pin_interrupt_configure,
	.manage_callback = gowin_empu_gpio_manage_callback,
	.get_pending_int = gowin_empu_gpio_get_pending_int};

static int gowin_empu_gpio_init(const struct device *dev)
{
	return 0;
}

#define GOWIN_EMPU_GPIO_INIT(n)                                                                    \
	static const struct gpio_gowin_empu_config gpio_gowin_empu_config##n = {                   \
		.common =                                                                          \
			{                                                                          \
				.port_pin_mask = GPIO_PORT_PIN_MASK_FROM_DT_INST(n),               \
			},                                                                         \
		.reg = (struct gpio_gowin_empu_reg *)DT_INST_REG_ADDR(n),                          \
	};                                                                                         \
	static struct gpio_gowin_empu_data gpio_gowin_empu_data##n;                                \
	DEVICE_DT_INST_DEFINE(n, &gowin_empu_gpio_init, NULL, &gpio_gowin_empu_data##n,            \
			      &gpio_gowin_empu_config##n, PRE_KERNEL_1, CONFIG_GPIO_INIT_PRIORITY, \
			      &gowin_empu_gpio_api);

DT_INST_FOREACH_STATUS_OKAY(GOWIN_EMPU_GPIO_INIT)
