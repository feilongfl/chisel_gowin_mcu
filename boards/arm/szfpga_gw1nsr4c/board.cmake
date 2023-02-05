# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

list(GET BOARD_ROOT 0 BOARD_ROOT_0)

board_runner_args(openFPGALoader "--logic=build/app_logic/impl/pnr/chisel.fs")
include(${BOARD_ROOT_0}/boards/common/openFPGALoader.board.cmake)
