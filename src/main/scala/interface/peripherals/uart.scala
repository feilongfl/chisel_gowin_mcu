package gowin.cpu.peripherals

import chisel3._
import chisel3.util._

class UART extends Bundle {
  val tx = Output(Bool())
  val rx = Input(Bool())
  val tick = Output(Clock())
}
