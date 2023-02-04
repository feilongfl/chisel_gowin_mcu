# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

zephyr_library()

zephyr_library_sources_ifdef(CONFIG_GPIO_GOWIN_EMPU gowin_empu_gpio.c)
