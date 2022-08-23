package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}

import fpgamacro.gowin._

class TangNano4k extends RawModule {
  /* Clock and reset */
  val clk_xtal = IO(Input(Clock()))
  val reset_button = IO(Input(Bool()))

  // debug port
  val dap = IO(new DAP()) // debug port

  /* Debug leds */
  val led = IO(Output(Bool()))

  withClockAndReset(clk_xtal, reset_button) {
    val mcu = Module(new EmcuModule())
    mcu.io.rtc_clk := clk_xtal
    mcu.io.dap <> dap
  }

  led := reset_button
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/"
  ))
}
