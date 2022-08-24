package fpgamacro.gowin

import chisel3._
import chisel3.util._

class SP extends BlackBox {
  val io = IO(new Bundle {
    val DO = Output(UInt(32.W))
    val DI = Input(UInt(32.W))
    val AD = Input(UInt(14.W))
    val WRE = Input(Bool())
    val CE = Input(Bool())
    val CLK = Input(Clock())
    val RESET = Input(Reset())
    val OCE = Input(Bool())
    val BLKSEL = Input(UInt(3.W))
  })
}
