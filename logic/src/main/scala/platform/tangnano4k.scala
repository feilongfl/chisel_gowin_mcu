package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}
import chisel3.experimental.Analog

import gowin.ips.Gowin_EMPU
import pio.{PIO_IIC, PIO_Uart, PIO_GPIO}

import primitives.gowin.basic.GOWIN_GSR

class TangNano4k extends Module {
  // debug led
  val led = IO(Output(UInt(1.W)))
  val iic = IO(new PIO_IIC())

  withReset(reset.asBool()) {
    // xtal freq: 27 MHz
    val emcu = Module(new Gowin_EMPU)
    val gsr = Module(new GOWIN_GSR)

    // peripherals - GPIO
    led := emcu.gpio.out & 0x01.U
    emcu.gpio <> DontCare
  }
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "../build/app_logic"
  ))
}
