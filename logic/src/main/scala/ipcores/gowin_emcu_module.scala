package gowin.ips

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

import pio.{PIO_IIC, PIO_Uart, PIO_GPIO, SRAM}

class Gowin_EMPU(freq: UInt = 27000000.U) extends Module {
  val gpio = IO(new PIO_GPIO(16))
  // val iic = IO(new PIO_IIC())
  // val uart = IO(new PIO_Uart())
  val mcu = Module(new Gowin_EMPU_Module())

  gpio <> mcu.peripherals.gpio
  // iic <> mcu.peripherals.iic
  // uart <> mcu.peripherals.uart0
}
