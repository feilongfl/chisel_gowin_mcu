# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

set(PROJECT_ROOT ${CMAKE_CURRENT_LIST_DIR}/..)

set(BOARD_DIR ${PROJECT_ROOT}/boards/${ARCH}/${BOARD})
message(STATUS "Board Arch: ${ARCH}")
message(STATUS "Board Dir: ${BOARD_DIR}")

set(LOGIC_DIR ${PROJECT_ROOT}/logic)

message(STATUS "Compile Chisel: ${LOGIC_DIR}")
execute_process(
  COMMAND sbt run
  WORKING_DIRECTORY ${LOGIC_DIR}
)

message(STATUS "Gowin Embedded io fix")
execute_process(
  COMMAND sed -i.bak -r "s/(input |output|inout )\\s+(\\w+hpram\\w+)/\\1 [0:0] \\2/" TangNano4k.v
  WORKING_DIRECTORY ${PROJECT_ROOT}/build/app_logic
)

message(STATUS "Generate Gowin Compile Script")
execute_process(
  COMMAND ${PROJECT_ROOT}/scripts/logic/gowin/gen_compilescript.bash ${PROJECT_ROOT} ${BOARD_DIR} ${BOARD} ${APPLICATION_SOURCE_DIR}
  OUTPUT_FILE ${PROJECT_ROOT}/build/app_logic/compile.gwsh
)

message(STATUS "Compile Verilog")
execute_process(
  COMMAND gw_sh compile.gwsh
  WORKING_DIRECTORY ${PROJECT_ROOT}/build/app_logic
)
set(DTC_OVERLAY_DIR ${PROJECT_ROOT}/build/app_logic/include)
list(APPEND DTS_ROOT ${DTC_OVERLAY_DIR})

message(STATUS "Generate DTS:" ${DTC_OVERLAY_DIR}/dts/${ARCH}/generated)
execute_process(
  COMMAND mkdir -p ${DTC_OVERLAY_DIR}/dts/${ARCH}/generated
  COMMAND bash -c "mv *.dtsi ${DTC_OVERLAY_DIR}/dts/${ARCH}/generated/"
  WORKING_DIRECTORY ${PROJECT_ROOT}/build/app_logic
)

message(STATUS "Merge Logic DTS:" ${DTC_OVERLAY_DIR}/dts/${ARCH}/generated/logic.dts)
execute_process(
  COMMAND bash -c "echo \"/* WARNING. THIS FILE IS AUTO-GENERATED. DO NOT MODIFY! */\" && ls *.dtsi | sed -r 's/.*/\#include \<generated\\/\\0\>/'"

  OUTPUT_FILE ${DTC_OVERLAY_DIR}/dts/${ARCH}/generated/logic.dts
  WORKING_DIRECTORY ${DTC_OVERLAY_DIR}/dts/${ARCH}/generated/
  COMMAND_ECHO STDOUT
)
