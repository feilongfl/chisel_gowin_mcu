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
  // led := mcu.io.gpio.output(0)
  mcu.io.gpio.output_enable <> DontCare
  // mcu.io.flash0 <> rom.flash0
  mcu.io.flash0 <> DontCare

  val addr = RegInit(0.U(32.W))
  val addr_max = 0x20.U;

  addr := addr + 1.U
  when(addr === addr_max) {
    addr := 0.U
  }

  rom.flash0.HADDR := addr
  led := rom.flash0.HRDATA(0) | mcu.io.gpio.output(0)

  rom.flash0.HSIZE <> DontCare
  rom.flash0.HSEL <> DontCare
  rom.flash0.HBURST <> DontCare
  rom.flash0.HREADYMUX <> DontCare
  rom.flash0.HTRANS <> DontCare
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/"
  ))
}
