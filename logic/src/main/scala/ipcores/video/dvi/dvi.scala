package ipcores.video.dvi

import chisel3._
import chisel3.util._

class DiffPair extends Bundle {
  val p = Bool()
  val n = Bool()
}

class VideoDviTmds extends Bundle {
  val clock = Output(UInt(10.W))
  val red = Output(UInt(10.W))
  val green = Output(UInt(10.W))
  val blue = Output(UInt(10.W))
}

class VideoRawPort extends Bundle {
  val red = Output(UInt(8.W))
  val green = Output(UInt(8.W))
  val blue = Output(UInt(8.W))

  val vs = Output(UInt(1.W))
  val hs = Output(UInt(1.W))
  val de = Output(UInt(1.W))
}

class VideoRawToDviTmds extends Module {
  val raw = IO(Flipped(new VideoRawPort()))
  val dvi = IO(new VideoDviTmds)

  val tmdsr_enc = Module(new TMDSEncoder)
  tmdsr_enc.io.en := raw.de
  tmdsr_enc.io.ctrl := raw.vs ## raw.hs
  tmdsr_enc.io.din := raw.red
  dvi.red := tmdsr_enc.io.dout

  val tmdsg_enc = Module(new TMDSEncoder)
  tmdsg_enc.io.en := raw.de
  tmdsg_enc.io.ctrl := raw.vs ## raw.hs
  tmdsg_enc.io.din := raw.green
  dvi.green := tmdsg_enc.io.dout

  val tmdsb_enc = Module(new TMDSEncoder)
  tmdsb_enc.io.en := raw.de
  tmdsb_enc.io.ctrl := raw.vs ## raw.hs
  tmdsb_enc.io.din := raw.blue
  dvi.blue := tmdsb_enc.io.dout

  dvi.clock := "b1111100000".U(10.W)
}

class Tmds extends Bundle {
  val clk = Bool()
  val red = Bool()
  val green = Bool()
  val blue = Bool()
}

class TMDSDiff extends Bundle {
  val clk = new DiffPair()
  val red = new DiffPair()
  val green = new DiffPair()
  val blue = new DiffPair()
}

class LVDS_OBUF extends BlackBox {
  val io = IO(new Bundle {
    val O = Output(Bool())
    val OB = Output(Bool())
    val I = Input(Bool())
  })
}

class TLVDS_OBUF extends LVDS_OBUF {}

/* OSER10 : serializer 10:1*/
class OSER10
    extends BlackBox(
      Map("GSREN" -> "false", "LSREN" -> "true")
    ) {
  val io = IO(new Bundle {
    val Q = Output(Bool()) // OSER10 data output signal
    val D0 = Input(Bool())
    val D1 = Input(Bool())
    val D2 = Input(Bool())
    val D3 = Input(Bool())
    val D4 = Input(Bool())
    val D5 = Input(Bool())
    val D6 = Input(Bool())
    val D7 = Input(Bool())
    val D8 = Input(Bool())
    val D9 = Input(Bool()) //  OSER10 data input signal
    val PCLK = Input(Clock()) // Primary clock input signal
    val FCLK = Input(Clock()) // High speed clock input signal
    val RESET = Input(Reset()) // Asynchronous reset input signal,
    // active-high.
  })
}

class Oser10Module extends Module {
  val io = IO(new Bundle {
    val q = Output(Bool())
    val data = Input(UInt(10.W))
    val fclk = Input(Clock()) // Fast clock
  })

  val osr10 = Module(new OSER10())
  io.q := osr10.io.Q
  osr10.io.D0 := io.data(0)
  osr10.io.D1 := io.data(1)
  osr10.io.D2 := io.data(2)
  osr10.io.D3 := io.data(3)
  osr10.io.D4 := io.data(4)
  osr10.io.D5 := io.data(5)
  osr10.io.D6 := io.data(6)
  osr10.io.D7 := io.data(7)
  osr10.io.D8 := io.data(8)
  osr10.io.D9 := io.data(9)
  osr10.io.PCLK := clock
  osr10.io.FCLK := io.fclk
  osr10.io.RESET := reset
}

class GowinHDMIChannel extends Module {
  val data = IO(Input(UInt(10.W)))
  val out = IO(Output(new DiffPair()))
  val serclk = IO(Input(Clock()))

  val ser10 = Module(new Oser10Module)
  ser10.io.data := data
  ser10.io.fclk := serclk

  val lvds = Module(new TLVDS_OBUF())
  lvds.io.I := ser10.io.q
  out.p := lvds.io.O
  out.n := lvds.io.OB
}

class GowinHDMI extends Module {
  val serclk = IO(Input(Clock()))

  val tmds_in = IO(Flipped(new VideoDviTmds))
  val tmds_out = IO(Output(new TMDSDiff))

  val channel_clock = Module(new GowinHDMIChannel)
  channel_clock.data := tmds_in.clock
  channel_clock.serclk := serclk
  tmds_out.clk := channel_clock.out

  val channel_red = Module(new GowinHDMIChannel)
  channel_red.data := tmds_in.red
  channel_red.serclk := serclk
  tmds_out.red := channel_red.out

  val channel_green = Module(new GowinHDMIChannel)
  channel_green.data := tmds_in.green
  channel_green.serclk := serclk
  tmds_out.green := channel_green.out

  val channel_blue = Module(new GowinHDMIChannel)
  channel_blue.data := tmds_in.blue
  channel_blue.serclk := serclk
  tmds_out.blue := channel_blue.out
}

class VideoRawToGowinHDMI extends Module {
  val raw = IO(Flipped(new VideoRawPort))
  val hdmi = IO(Output(new TMDSDiff))
  val serclk = IO(Input(Clock()))

  val encode = Module(new VideoRawToDviTmds)
  val port = Module(new GowinHDMI)

  encode.raw <> raw
  encode.dvi <> port.tmds_in
  port.serclk := serclk
  port.tmds_out <> hdmi
}
