# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
# SPDX-License-Identifier: Apache-2.0

set(model_src_dir ${CMAKE_CURRENT_SOURCE_DIR}/model)
set(model_build_dir ${CMAKE_CURRENT_BINARY_DIR}/model)

set(MODEL_LIB_DIR ${model_build_dir}/lib)
set(MODEL_INCLUDE_DIR ${model_src_dir}/simulink_led_ert_rtw)

if(CMAKE_GENERATOR STREQUAL "Unix Makefiles")
  # https://www.gnu.org/software/make/manual/html_node/MAKE-Variable.html
  set(submake "$(MAKE)")
else() # Obviously no MAKEFLAGS. Let's hope a "make" can be found somewhere.
  set(submake "make")
endif()

zephyr_get_include_directories_for_lang_as_string(C includes)
zephyr_get_system_include_directories_for_lang_as_string(C system_includes)
zephyr_get_compile_definitions_for_lang_as_string(C definitions)
zephyr_get_compile_options_for_lang_as_string(C options)
set(external_project_cflags
  "${includes} ${definitions} ${options} ${system_includes}"
)

ExternalProject_Add(
  project_simulink_led # Name for custom target
  PREFIX ${model_build_dir} # Root dir for entire project
  SOURCE_DIR ${model_src_dir}
  BINARY_DIR ${model_src_dir} # This particular build system is invoked from the root
  CONFIGURE_COMMAND "" # Skip configuring the project, e.g. with autoconf
  BUILD_COMMAND
  ${submake}
  PREFIX=${model_build_dir}
  CC=${CMAKE_C_COMPILER}
  AR=${CMAKE_AR}
  CFLAGS=${external_project_cflags}
  INSTALL_COMMAND "" # This particular build system has no install command
  BUILD_BYPRODUCTS ${MODEL_LIB_DIR}/libsimulink_led.a
)

add_library(lib_simulink_led STATIC IMPORTED GLOBAL)
add_dependencies(
  lib_simulink_led
  project_simulink_led
)
set_target_properties(lib_simulink_led PROPERTIES IMPORTED_LOCATION ${MODEL_LIB_DIR}/libsimulink_led.a)
set_target_properties(lib_simulink_led PROPERTIES INTERFACE_INCLUDE_DIRECTORIES ${MODEL_INCLUDE_DIR})

target_link_libraries(app PUBLIC lib_simulink_led)
