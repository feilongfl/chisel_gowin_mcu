# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

cmake_minimum_required(VERSION 3.20.0)
set(PROJECT_ROOT ${CMAKE_CURRENT_LIST_DIR}/../../..)

# todo: generate from ip later
set(ARCH arm)
include(${PROJECT_ROOT}/cmake/project.cmake)

find_package(Zephyr REQUIRED HINTS $ENV{ZEPHYR_BASE})
project(led_blinky)

include(${CMAKE_CURRENT_LIST_DIR}/model.cmake)

target_sources(app PRIVATE src/main.c)
