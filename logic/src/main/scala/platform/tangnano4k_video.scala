package gowin.platforms

import chisel3._
import chisel3.util._
import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}
import chisel3.experimental.Analog

import gowin.ipcores.cpu.gowin.empu.Gowin_EMPU
import gowin.ipcores.cpu.gowin.video.video_top
import pio.{PIO_IIC, PIO_Uart, PIO_GPIO}

class GPIO_IIC extends BlackBox with HasBlackBoxInline {
  val io = IO(new Bundle {
    val gpio_sda = Input(UInt(1.W))
    val gpio_scl = Input(UInt(1.W))

    val scl = Analog(1.W)
    val sda = Analog(1.W)
  })

  setInline(
    "gpio_iic.v",
    """
  |module GPIO_IIC (
  |  input gpio_sda,
  |  input gpio_scl,
  |  inout scl,
  |  inout sda
  |);
  |
  |  assign scl = (gpio_scl == 1)? 1'bz : 1'b0;
  |  assign sda = (gpio_sda == 1)? 1'bz : 1'b0;
  |
  |endmodule
  |""".stripMargin
  )
}

class TangNano4k extends Module {
  // debug led
  val led = IO(Output(UInt(1.W)))
  val iic = IO(new PIO_IIC())
  val uart_tx = IO(Output(UInt(1.W)))

  // video port
  val I_clk = Input(Clock())
  val I_rst_n = Input(Reset())

  // val O_led = Output(UInt(2.W))
  // val iic_data = IO(Output(UInt(1.W)))
  // val iic_clock = IO(Output(UInt(1.W)))
  val VSYNC = IO(Input(UInt(1.W)))
  val HREF = IO(Input(UInt(1.W)))

  val PIXDATA = IO(Input(UInt(10.W)))
  val PIXCLK = IO(Input(Clock()))
  val XCLK = IO(Output(Clock()))

  val O_hpram_ck = IO(Output(UInt(1.W)))
  val O_hpram_ck_n = IO(Output(UInt(1.W)))
  val O_hpram_cs_n = IO(Output(UInt(1.W)))
  val O_hpram_reset_n = IO(Output(UInt(1.W)))
  val IO_hpram_dq = IO(Analog(8.W))
  val IO_hpram_rwds = IO(Analog(1.W))

  val O_tmds_clk_p = IO(Output(UInt(1.W)))
  val O_tmds_clk_n = IO(Output(UInt(1.W)))
  val O_tmds_data_p = IO(Output(UInt(3.W)))
  val O_tmds_data_n = IO(Output(UInt(3.W)))
  // video port

  val bbb = Module(new video_top())
  bbb.io.I_clk := clock
  bbb.io.I_rst_n := reset

  bbb.io.O_led <> DontCare
  bbb.io.SDA <> DontCare
  bbb.io.SCL <> DontCare
  bbb.io.VSYNC <> VSYNC
  bbb.io.HREF <> HREF
  bbb.io.PIXDATA <> PIXDATA
  bbb.io.PIXCLK <> PIXCLK
  bbb.io.XCLK <> XCLK
  bbb.io.O_hpram_ck <> O_hpram_ck
  bbb.io.O_hpram_ck_n <> O_hpram_ck_n
  bbb.io.O_hpram_cs_n <> O_hpram_cs_n
  bbb.io.O_hpram_reset_n <> O_hpram_reset_n
  bbb.io.IO_hpram_dq <> IO_hpram_dq
  bbb.io.IO_hpram_rwds <> IO_hpram_rwds
  bbb.io.O_tmds_clk_p <> O_tmds_clk_p
  bbb.io.O_tmds_clk_n <> O_tmds_clk_n
  bbb.io.O_tmds_data_p <> O_tmds_data_p
  bbb.io.O_tmds_data_n <> O_tmds_data_n

  withReset(reset.asBool()) {
    // xtal freq: 27 MHz
    val emcu = Module(new Gowin_EMPU())

    // peripherals - GPIO
    led := emcu.gpio.out & 0x01.U
    emcu.gpio <> DontCare

    val gpio_iic = Module(new GPIO_IIC)
    gpio_iic.io.gpio_sda := (emcu.gpio.out >> 1.U) & 0x01.U
    gpio_iic.io.gpio_scl := (emcu.gpio.out >> 2.U) & 0x01.U
    gpio_iic.io.scl <> iic.clock
    gpio_iic.io.sda <> iic.data
    // iic_data := (emcu.gpio.out >> 1.U) & 0x01.U
    // iic_clock := (emcu.gpio.out >> 2.U) & 0x01.U

    // iic <> emcu.iic
    emcu.iic <> DontCare
    uart_tx <> emcu.uart.tx
    emcu.uart.rx <> DontCare
  }
}

object TangNano4k extends App {
  (new ChiselStage) emitVerilog (new TangNano4k(), Array(
    "--target-dir",
    "../build/app_logic"
  ))
}
