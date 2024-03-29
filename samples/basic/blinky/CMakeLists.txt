# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

cmake_minimum_required(VERSION 3.20.0)

set(PROJECT_ROOT ${CMAKE_CURRENT_LIST_DIR}/../../..)

set(BOARD_ROOT ${PROJECT_ROOT})
set(SOC_ROOT ${PROJECT_ROOT})
set(DTS_ROOT ${PROJECT_ROOT})
set(LOGIC_ROOT ${PROJECT_ROOT})

list(APPEND ZEPHYR_EXTRA_MODULES
  ${PROJECT_ROOT}/drivers
)

find_package(Zephyr REQUIRED HINTS $ENV{ZEPHYR_BASE})
project(out_of_tree_board)

list(GET BOARD_ROOT 0 BOARD_ROOT_0)
set(BOARD_DIR ${BOARD_ROOT_0}/boards/${ARCH}/${BOARD})
message(STATUS "FPGABoard: ${BOARD_DIR}")

set(app_logic_root_dir ${LOGIC_ROOT})
set(app_logic_src_dir ${app_logic_root_dir}/platform/chisel)
set(app_logic_build_dir ${CMAKE_CURRENT_BINARY_DIR}/app_logic)
ExternalProject_Add(
  app_logic # Name for custom target
  PREFIX ${app_logic_root_dir} # Root dir for entire project
  SOURCE_DIR ${app_logic_root_dir}
  BINARY_DIR ${app_logic_root_dir}
  CONFIGURE_COMMAND ""
  BUILD_COMMAND
  echo run sbt compile for logic...
  && pushd ${app_logic_root_dir}/logic
  && sbt run
  && popd
  && echo "#!/usr/bin/gw_sh" > ${app_logic_build_dir}/compile.gwsh
  && find ${app_logic_build_dir} -name "*.v" | sed -r "s/^/add_file /" >> ${app_logic_build_dir}/compile.gwsh
  && find ${BOARD_DIR} -name "*.cst" | sed -r "s/^/add_file /" >> ${app_logic_build_dir}/compile.gwsh
  && find ${BOARD_DIR} -name "*.sdc" | sed -r "s/^/add_file /" >> ${app_logic_build_dir}/compile.gwsh
  && cat ${CMAKE_CURRENT_LIST_DIR}/logic/${BOARD}.gwsh >> ${app_logic_build_dir}/compile.gwsh
  && echo prepare gowin compile ...
  && cat ${app_logic_build_dir}/compile.gwsh
  && echo run gowin compile ...
  && pushd ${app_logic_build_dir}
  && gw_sh ${app_logic_build_dir}/compile.gwsh
  && popd
  INSTALL_COMMAND ""
  BUILD_BYPRODUCTS ${app_logic_build_dir}/impl/pnr/chisel.fs
)

target_sources(app PRIVATE src/main.c)
add_dependencies(
  app
  app_logic
)
