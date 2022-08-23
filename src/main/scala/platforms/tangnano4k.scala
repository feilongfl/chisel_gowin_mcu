package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}

import fpgamacro.gowin._

class BasicIO extends Bundle {
  val clk_xtal = Input(Clock())
  val reset_button = Input(Bool())

  /* Debug leds */
  val led = Output(Bool())
}

class TangNano4k extends RawModule {
  // Basic IO
  val io = IO(new BasicIO())

  // debug port
  val dap = IO(new DAP()) // debug port

  withClockAndReset(io.clk_xtal, io.reset_button) {
    val mcu = Module(new EmcuModule())
    mcu.io.rtc_clk := io.clk_xtal
    mcu.io.dap <> dap
    mcu.io.gpio <> DontCare
  }

  io.led := io.reset_button
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/"
  ))
}
