package ipcores.video.dvi

import chisel3._
import chisel3.util._

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
  val raw = IO(Flipped(VideoRawPort))
  val dvi = IO(VideoDviTmds)

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

class DiffPair extends Bundle {
  val p = Bool()
  val n = Bool()
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
  val data = Input(UInt(10.W))
  val out = Output(DiffPair())
  val serclk = Input(Clock())

  val ser10 = Module(new Oser10Module)
  ser10.data := data
  ser10.fclk := serclk

  val lvds = Module(TLVDS_OBUF())
  lvds.I := ser10.q
  out.P := lvds.O
  out.N := lvds.OB
}

class GowinHDMI extends Module {
  val serclk = IO(Input(Clock()))

  val tmds_in = IO(new VideoDviTmds)
  val tmds_out = IO(new TMDSDiff)

  val channel_clock = Module(new GowinHDMIChannel)
  channel_clock.data := tmds_in.clock
  channel_clock.serclk := tmds_in.serclk
  tmds_out.clk := channel_clock.out

  val channel_red = Module(new GowinHDMIChannel)
  channel_red.data := tmds_in.red
  channel_red.serclk := tmds_in.serclk
  tmds_out.clk := channel_red.out

  val channel_green = Module(new GowinHDMIChannel)
  channel_green.data := tmds_in.green
  channel_green.serclk := tmds_in.serclk
  tmds_out.clk := channel_green.out

  val channel_blue = Module(new GowinHDMIChannel)
  channel_blue.data := tmds_in.blue
  channel_blue.serclk := tmds_in.serclk
  tmds_out.clk := channel_blue.out
}

class VideoRawToGowinHDMI extends Module {
  val raw = IO(Flipped(VideoRawPort))
  val hdmi = IO(new TMDSDiff)
  val serclk = IO(Input(Clock()))

  val encode = Module(new VideoRawToDviTmds)
  val port = Module(new GowinHDMI)

  encode.raw <> raw
  encode.dvi <> port.tmds_in
  port.serclk := serclk
  port.tmds_out <> hdmi
}
