package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}

import fpgamacro.gowin._
import arm.amba3._
import components.rom.AHBROM_Logic

class TangNano4k extends Module {
  // debug led
  val led = IO(Output(Bool()))

  // emcu
  val mcu = Module(new EmcuModule())
  val rom = Module(new AHBROM_Logic)

  mcu.io.rtc_clk := clock
  mcu.io.gpio.input <> 0x5555.U(16.W)
  mcu.io.gpio.output_enable <> DontCare
  mcu.io.flash0 <> rom.flash0

  led := mcu.io.gpio.output(0)
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/"
  ))
}
