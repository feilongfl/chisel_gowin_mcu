create_clock -name clock -period 37.037 -waveform {0 18.518} [get_ports {clock}]
create_clock -name serial_clock -period 2.694 -waveform {0 1.347} [get_nets {bbb_serial_clk}]
create_clock -name pix_clk -period 13.468 -waveform {0 6.734} [get_ports {PIXCLK}] -add
