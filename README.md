# Unnamed

> This Project is working in process.

This project helps developers create FPGA based systems with [zephyr](https://github.com/zephyrproject-rtos/zephyr) rtos.
The goal of this project is to build Zephyr RTOS in a way similar to Xilinx's [PetaLinux](https://www.xilinx.com/products/design-tools/embedded-software/petalinux-sdk.html) workflow.

## Getting Started

The current project also depends on the following programs:
- [Zephyr](https://github.com/zephyrproject-rtos/zephyr)
- [Chisel](https://github.com/chipsalliance/chisel3/blob/master/SETUP.md)
- [GowinIDE](http://www.gowinsemi.com.cn/faq.aspx)
  - sorry for paste chinese version website here, I'm not sure where this package located on website in english.
- [openFPGALoader](https://trabucayre.github.io/openFPGALoader/guide/install.html)
  - Zephyr not support `openFPGALoader` at this time, so you need apply [patches/zephyr/0001-add-support-for-openFPGAloader.patch](patches/zephyr/0001-add-support-for-openFPGAloader.patch) to your zephyr project.

This project is in a very early stage and there are no plans to maintain documentation.
If you want to try this project, please refer to the installation documentation of chisel and zephyr.
It is highly recommended to use a linux development environment, currently no plans to support a windows development environment.

## Support devices

Currently focusing on architecture design, only supports [Sipeed TangNano4k](https://wiki.sipeed.com/hardware/en/tang/Tang-Nano-4K/Nano-4K.html).
It is based on the GW1NSR-4C chip of Chinese semiconductor manufacturer GOWIN. Its development and compilation tools are small in size and easy to install.

### [Sipeed TangNano4k](https://wiki.sipeed.com/hardware/en/tang/Tang-Nano-4K/Nano-4K.html)

``` shell
# compile
west build -p always -b tangnano4k .
# flash to board
west flash
```

Then the led near HDMI interface is blinking.
