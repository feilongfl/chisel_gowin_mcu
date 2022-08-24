package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}

import fpgamacro.gowin._

class BasicIO extends Bundle {
  val clk_xtal = Input(Clock())
  val reset_button = Input(Bool())

  /* Debug leds */
}

class BSRAM_SP_Module extends Module {
  val ram = IO(Flipped(new SRAM))

  val bsram0 = Module(new SP)
  val bsram1 = Module(new SP)
  val bsram2 = Module(new SP)
  val bsram3 = Module(new SP)

  bsram0.io.CLK := clock
  bsram1.io.CLK := clock
  bsram2.io.CLK := clock
  bsram3.io.CLK := clock

  bsram0.io.RESET := reset
  bsram1.io.RESET := reset
  bsram2.io.RESET := reset
  bsram3.io.RESET := reset

  bsram0.io.CE := ram.cs
  bsram1.io.CE := ram.cs
  bsram2.io.CE := ram.cs
  bsram3.io.CE := ram.cs

  bsram0.io.WRE := ram.wren(0)
  bsram1.io.WRE := ram.wren(1)
  bsram2.io.WRE := ram.wren(2)
  bsram3.io.WRE := ram.wren(3)

  bsram0.io.AD := Cat(ram.addr, 0.U(1.W))
  bsram1.io.AD := Cat(ram.addr, 0.U(1.W))
  bsram2.io.AD := Cat(ram.addr, 0.U(1.W))
  bsram3.io.AD := Cat(ram.addr, 0.U(1.W))

  ram.rdata := bsram0.io.DO
  ram.rdata := bsram1.io.DO
  ram.rdata := bsram2.io.DO
  ram.rdata := bsram3.io.DO

  bsram0.io.DI := ram.wdata
  bsram1.io.DI := ram.wdata
  bsram2.io.DI := ram.wdata
  bsram3.io.DI := ram.wdata
}

class TangNano4k extends Module {
  // debug led
  val led = IO(Output(Bool()))

  // GPIO
  val gpio = IO(Output(Bool()))

  // debug port
  // val dap = IO(new DAP()) // debug port

  // withClockAndReset(io.clk_xtal, io.reset_button) {
  val mcu = Module(new EmcuModule())
  val sram0 = Module(new BSRAM_SP_Module)

  mcu.io.rtc_clk := clock
  mcu.io.dap <> DontCare
  mcu.io.gpio.input <> 0x5555.U(16.W)
  mcu.io.gpio.output_enable <> DontCare

  gpio := mcu.io.gpio.output(0)

  mcu.io.sram0 <> sram0.ram

  led := reset
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/"
  ))
}
