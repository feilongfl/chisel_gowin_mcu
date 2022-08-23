# GOWIN MCU (chisel version)  
** WORK IN PROCESS **
=======================

> This project aims to implement a Hardware-IP MCU package for GOWIN FPGAs through chisel,  
> and to implement an automatic device tree generation function to integrate with [zephyr RTOS](https://zephyrproject.org/).  
> (similar to that of xilinx in [petalinux](https://www.xilinx.com/products/design-tools/embedded-software/petalinux-sdk.html))

## Roadmap

The project has just started, and in order to avoid a lot of rework due to architectural design errors, we only want to support [Tang Nano 4k](https://wiki.sipeed.com/hardware/en/tang/Tang-Nano-4K/Nano-4K.html) board at this stage.

- [ ] GOWIN FPGA
  - [ ] GOWIN MACRO
    - [ ] MCU
      - [ ] EMCU
      - [ ] MCU
    - [ ] ROM
    - [ ] RAM
    - [ ] Peripherals
      - [ ] GPIO
      - [ ] UART
    - [ ] Debug
      - [ ] TPIU
      - [ ] DAP
    - [ ] Clock
      - [ ] PLL
  - [ ] GOWIN COMPILE CHAN
    - [ ] compiler
    - [ ] downloader
    - [ ] debugger
  - [ ] GOWIN COMPILE CI
    - [ ] github action
- [ ] ARM AMBA
  - [ ] AHB
    - [ ] master
    - [ ] slave
  - [ ] APB
    - [ ] master
  - [ ] AXI
    - [ ] AHB to AXI-Lite

## compile and download

At this time, this project can't compile correctly.

GOWIN make there IP as blackbox,
I'm working for how to connect MCU without GOWIN blackbox.

Currently I'm reading the GOWIN manual to learn how to use the relevant proto language and wrap it with chisel.
