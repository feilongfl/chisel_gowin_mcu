%%% Led Blinky

%% prepare project
close all;
clear;

h = new_system('simulink_led', 'Model');
load_system(h);

%% Add Blocks
add_block('simulink/Sources/Pulse Generator', 'simulink_led/led_pluse');
add_block('simulink/Sinks/Out1', 'simulink_led/out_led');

%% Set parameter
set_param('simulink_led/led_pluse', 'Amplitude', '1');
set_param('simulink_led/led_pluse', 'PulseWidth', '50');
set_param('simulink_led/led_pluse', 'Period', '2');
set_param('simulink_led/led_pluse', 'PhaseDelay', '0');

%% link block
add_line(h, 'led_pluse/1', 'out_led/1');

% Fix simulink layout
Simulink.BlockDiagram.arrangeSystem(h);

%% Config C code
cs = getActiveConfigSet(h);
switchTarget(cs, 'ert.tlc', []);
cs.set_param('Solver', 'FixedStepAuto');
cs.set_param('GenCodeOnly', 'on');
cs.set_param('ProdHWDeviceType', 'ARM Compatible->ARM Cortex-M');
% cs.set_param('ProdHWDeviceType', 'RISC-V->RV32I');
% cs.set_param('ProdHWDeviceType', 'RISC-V->RV64I');

% check config
% openDialog(cs);
% saveAs(cs, 'model_config');

%% Generate C code
rtwbuild(h);

%% Exit without save
close_system(h, 0);
