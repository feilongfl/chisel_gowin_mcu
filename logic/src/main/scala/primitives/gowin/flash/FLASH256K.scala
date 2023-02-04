package primitives.gowin.flash

import chisel3._
import chisel3.util._

class FLASH256K extends BlackBox {
  val io = IO(new Bundle {
    // DOUT[31:0]Output数据输出总线。
    val DOUT = Output(UInt(32.W));
    // DIN[31:0]Input数据输入总线。
    val DIN = Input(UInt(32.W));
    // XADR[6:0]InputX 地址总线，访问行地址，其中 XADR[n:3]用于选
    // 择某一页，XADR[2:0]用于选择一页中的某一行，一
    // 页由 8 行组成，一行由 64 列组成。
    val XADR = Input(UInt(7.W));
    // YADR[5:0]InputY 地址总线，用于选择一行存储单元中的某一列，一行由 64 列组成。
    val YADR = Input(UInt(6.W));
    // XEInputX 地址使能信号，当 XE 为 0 时，所有行地址均不使能。
    val XEInputX = Input(UInt(1.W));
    // YEInputY 地址使能信号，当 YE 为 0 时，所有列地址均不使能。
    val YEInputY = Input(UInt(1.W));
    // SEInput 检测放大器使能信号，高电平有效。
    val SEInput = Input(UInt(1.W));
    // PROGInput 写信号，高电平有效。
    val PROGInput = Input(UInt(1.W));
    // ERASEInput 擦除信号，高电平有效。
    val ERASEInput = Input(UInt(1.W));
    // NVSTRInputFlash 数据存储信号，高电平有效。
    val NVSTRInputFlash = Input(UInt(1.W));
  })
}
