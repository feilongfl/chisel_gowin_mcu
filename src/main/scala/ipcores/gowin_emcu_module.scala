package gowin.ips

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

import pio.{PIO_IIC, PIO_Uart}

class Gowin_EMPU_Top(gpio_width: Int = 16) extends BlackBox {
  val io = IO(new Bundle {
    val sys_clk = Input(Clock())
    val reset_n = Input(Reset())

    val gpio = Analog(gpio_width.W)
    val scl = Analog(1.W)
    val sda = Analog(1.W)
    val uart0_txd = Output(UInt(1.W))
    val uart0_rxd = Input(UInt(1.W))
  })
}

class Gowin_EMPU_Module(gpio_width: Int = 16) extends Module {
  val emcu = Module(new Gowin_EMPU_Top(gpio_width))

  emcu.io.sys_clk := clock
  emcu.io.reset_n := reset

  val peripherals = IO(new Bundle {
    val gpio = Analog(gpio_width.W)
    val iic = new PIO_IIC()
    val uart0 = new PIO_Uart()
  })
  peripherals.gpio <> emcu.io.gpio
  peripherals.iic.clock <> emcu.io.scl
  peripherals.iic.data <> emcu.io.sda
  peripherals.uart0.tx <> emcu.io.uart0_txd
  peripherals.uart0.rx <> emcu.io.uart0_rxd
}

class Gowin_EMPU(freq: UInt = 27000000.U) extends Module {
  val gpio = IO(Analog(16.W))
  val iic = IO(new PIO_IIC())
  val uart = IO(new PIO_Uart())
  val mcu = Module(new Gowin_EMPU_Module())

  gpio <> mcu.peripherals.gpio
  iic <> mcu.peripherals.iic
  uart <> mcu.peripherals.uart0
}
