package gowin.cpu.peripherals

import chisel3._
import chisel3.util._

class GPIO extends Bundle {
  val input = Input(UInt(16.W)) // IOEXPINPUTI
  val output = Output(UInt(16.W)) // IOEXPOUTPUTO
  val output_enable = Output(UInt(16.W)) // IOEXPOUTPUTENO
}
