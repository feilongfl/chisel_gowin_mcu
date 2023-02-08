#
# Copyright (c) 2023 YuLong Yao <feilongphone@gmail.com>
#
# SPDX-License-Identifier: Apache-2.0
#

PREFIX ?= .
OBJ_DIR ?= $(PREFIX)/obj
LIB_DIR ?= $(PREFIX)/lib

RTW_DIR ?= simulink_led_ert_rtw
MODEL_NAME ?= simulink_led

ifeq ($(findstring Windows, $(OS)),Windows)
	DEL := rmdir /S /Q
	MKDIR := mkdir
else
	DEL := rm -rf
	MKDIR := mkdir -p
endif

all: $(RTW_DIR)/simulink_led.c
	-$(MKDIR) "$(OBJ_DIR)"
	-$(MKDIR) "$(LIB_DIR)"
	$(CC) -c $(CFLAGS) -MD -I$(RTW_DIR) $(RTW_DIR)/$(MODEL_NAME).c -o $(OBJ_DIR)/$(MODEL_NAME).o
	$(AR) -rcs $(LIB_DIR)/lib$(MODEL_NAME).a $(OBJ_DIR)/$(MODEL_NAME).o

$(RTW_DIR)/$(MODEL_NAME).c: model.m
	matlab -nodesktop -nosplash -r "run model.m; exit;"

clean:
	$(DEL) "$(OBJ_DIR)" "$(LIB_DIR)" "$(RTW_DIR)/$(MODEL_NAME).c"
