package arm.dap

import chisel3._
import chisel3.util._

class DAP extends Bundle {
  val tms = Input(Bool()) // Debug TMS
  val tdi = Input(Bool()) // Debug TDI
  val trst = Input(Bool()) // Test reset
  val tclk = Input(Clock()) // Test clock / SWCLK
  val tdo = Output(Bool()) // Debug TDO
  val tdo_en = Output(Bool()) // TDO output pad control signal

  // JTAG or Serial-Wire selection JTAG mode(1) or SW mode(0)
  val sw = Output(Bool())
}

class TPIU extends Bundle {
  val clk = Output(Clock())
  val data = Output(UInt(4.W))
}
