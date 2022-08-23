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

  // GPIO
  val gpio = IO(Output(Bool()))

  // debug port
  // val dap = IO(new DAP()) // debug port

  withClockAndReset(io.clk_xtal, io.reset_button) {
    val mcu = Module(new EmcuModule())
    mcu.io.rtc_clk := io.clk_xtal
    mcu.io.dap <> DontCare
    mcu.io.gpio.input <> 0x5555.U(16.W)
    mcu.io.gpio.output <> DontCare
    mcu.io.gpio.output_enable <> DontCare

    gpio := mcu.io.gpio.output(0)
  }

  io.led := io.reset_button
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/"
  ))
}
