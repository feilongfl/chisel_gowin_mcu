# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

set(PROJECT_ROOT ${CMAKE_CURRENT_LIST_DIR}/..)

set(BOARD_ROOT ${PROJECT_ROOT})
set(SOC_ROOT ${PROJECT_ROOT})

# set(DTS_ROOT ${PROJECT_ROOT})
list(APPEND DTS_ROOT ${PROJECT_ROOT})
set(LOGIC_ROOT ${PROJECT_ROOT})

list(APPEND ZEPHYR_EXTRA_MODULES ${PROJECT_ROOT}/drivers)
list(APPEND zephyr_cmake_modules ${PROJECT_ROOT}/cmake/logic.cmake)
