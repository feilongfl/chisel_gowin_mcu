#!/usr/bin/env bash

echo "#!/usr/bin/gw_sh"

export PROJECT_ROOT=$1
export BOARD_DIR=$2
export BOARD=$3
export APPLICATION_SOURCE_DIR=$4

find ${PROJECT_ROOT}/build/app_logic -name "*.v" | sed -r "s/^/add_file /"
find ${BOARD_DIR} -name "*.cst" | sed -r "s/^/add_file /"
find ${BOARD_DIR} -name "*.sdc" | sed -r "s/^/add_file /"
cat ${APPLICATION_SOURCE_DIR}/logic/${BOARD}.gwsh
