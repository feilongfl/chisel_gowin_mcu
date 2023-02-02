package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}
import chisel3.experimental.Analog

import gowin.ips.Gowin_EMPU
import pio.{PIO_IIC, PIO_Uart}

class TangNano4kGPIO extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val mcu_gpio = Analog(16.W)

    val led = Analog(1.W)
  })

  setInline(
    "TangNano4kGPIO.v",
    """
  |module TangNano4kGPIO(
  |  inout[15:0]   mcu_gpio,
  |  inout         led
  |);
  |  assign mcu_gpio[0] = led;
  |endmodule
  |""".stripMargin
  )
}

class TangNano4k extends Module {
  // debug led
  val led = IO(Analog(1.W))
  val iic = IO(new PIO_IIC())
  val uart_tx = IO(Output(UInt(1.W)))

  withReset(reset.asBool()) {
    // xtal freq: 27 MHz
    val emcu = Module(new Gowin_EMPU())

    // peripherals - GPIO
    val gpio = Module(new TangNano4kGPIO())
    gpio.io.mcu_gpio <> emcu.gpio
    led <> gpio.io.led

    iic <> emcu.iic
    uart_tx <> emcu.uart.tx
    emcu.uart.rx <> DontCare
  }
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "build/app_logic"
  ))
}
