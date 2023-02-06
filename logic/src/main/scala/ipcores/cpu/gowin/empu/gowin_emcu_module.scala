package gowin.ipcores.cpu.gowin.empu

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

import pio.{PIO_IIC, PIO_Uart, PIO_GPIO}

class Gowin_EMPU_Top(gpio_width: Int = 16)
    extends BlackBox
    with HasBlackBoxPath {
  val io = IO(new Bundle {
    val sys_clk = Input(Clock())
    val reset_n = Input(Reset())

    val gpioin = Input(UInt(gpio_width.W))
    val gpioout = Output(UInt(gpio_width.W))
    val gpioouten = Output(UInt(gpio_width.W))

    val scl = Analog(1.W)
    val sda = Analog(1.W)
    val uart0_txd = Output(UInt(1.W))
    val uart0_rxd = Input(UInt(1.W))
  })

  addPath("./src/main/scala/ipcores/cpu/gowin/empu/gowin_empu.v")
  addPath("./src/main/scala/ipcores/cpu/gowin/empu/gowin_empu.dtsi")
}

class Gowin_EMPU_Module(gpio_width: Int = 16) extends Module {
  val emcu = Module(new Gowin_EMPU_Top(gpio_width))

  emcu.io.sys_clk := clock
  emcu.io.reset_n := reset

  val peripherals = IO(new Bundle {
    val gpio = new PIO_GPIO(gpio_width)
    val iic = new PIO_IIC()
    val uart0 = new PIO_Uart()
  })
  peripherals.gpio.in <> emcu.io.gpioin
  peripherals.gpio.out <> emcu.io.gpioout
  peripherals.gpio.outen <> emcu.io.gpioouten

  peripherals.iic.clock <> emcu.io.scl
  peripherals.iic.data <> emcu.io.sda

  peripherals.uart0.tx <> emcu.io.uart0_txd
  peripherals.uart0.rx <> emcu.io.uart0_rxd
}

class Gowin_EMPU(freq: UInt = 27000000.U) extends Module {
  val gpio = IO(new PIO_GPIO(16))
  val iic = IO(new PIO_IIC())
  val uart = IO(new PIO_Uart())
  val mcu = Module(new Gowin_EMPU_Module())

  gpio <> mcu.peripherals.gpio
  iic <> mcu.peripherals.iic
  uart <> mcu.peripherals.uart0
}
