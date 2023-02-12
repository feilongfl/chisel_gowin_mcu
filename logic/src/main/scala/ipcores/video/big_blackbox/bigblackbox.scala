package gowin.ipcores.cpu.gowin.video

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

class video_top extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle {
    val I_clk = Input(Clock())
    val I_rst_n = Input(Reset())

    val VSYNC = Input(UInt(1.W))
    val HREF = Input(UInt(1.W))

    val PIXDATA = Input(UInt(10.W))
    val PIXCLK = Input(Clock())
    val XCLK = Output(Clock())

    val O_hpram_ck = Output(UInt(1.W))
    val O_hpram_ck_n = Output(UInt(1.W))
    val O_hpram_cs_n = Output(UInt(1.W))
    val O_hpram_reset_n = Output(UInt(1.W))
    val IO_hpram_dq = Analog(8.W)
    val IO_hpram_rwds = Analog(1.W)

    // val O_tmds_clk_p = Output(UInt(1.W))
    // val O_tmds_clk_n = Output(UInt(1.W))
    // val O_tmds_data_p = Output(UInt(3.W))
    // val O_tmds_data_n = Output(UInt(3.W))

    val serial_clk = Output(Clock())
    val rgb_vs = Output(UInt(1.W))
    val rgb_hs = Output(UInt(1.W))
    val rgb_de = Output(UInt(1.W))
    val rgb_data = Output(UInt(24.W))
  })

  addPath("./src/main/scala/ipcores/video/big_blackbox/video_top.v")
  addPath("./src/main/scala/ipcores/video/big_blackbox/testpattern.v")
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/ov2640/OV2640_Registers.v"
  )
  addPath("./src/main/scala/ipcores/video/big_blackbox/ov2640/I2C_Interface.v")
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/ov2640/OV2640_Controller.v"
  )
  addPath("./src/main/scala/ipcores/video/big_blackbox/syn_code/syn_gen.v")
  addPath("./src/main/scala/ipcores/video/big_blackbox/gowin_pllvr/GW_PLLVR.v")
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/gowin_pllvr/TMDS_PLLVR.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/video_frame_buffer/temp/VFB/top_define.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/video_frame_buffer/temp/VFB/vfb_defines.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/video_frame_buffer/video_frame_buffer.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/dvi_tx/temp/DviTx/top_define.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/dvi_tx/temp/DviTx/dvi_tx_defines.v"
  )
  addPath("./src/main/scala/ipcores/video/big_blackbox/dvi_tx/dvi_tx.v")
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/hyperram_memory_interface/hyperram_memory_interface.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/hyperram_memory_interface/temp/HYPERRAM/hpram_define.v"
  )
  addPath(
    "./src/main/scala/ipcores/video/big_blackbox/hyperram_memory_interface/temp/HYPERRAM/hpram_param.v"
  )
}
