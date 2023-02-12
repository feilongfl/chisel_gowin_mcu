create_clock -name clock -period 37.037 -waveform {0 18.518} [get_ports {clock}]
create_clock -name serial_clock -period 2.694 -waveform {0 1.347} [get_nets {bbb_serial_clk}]
