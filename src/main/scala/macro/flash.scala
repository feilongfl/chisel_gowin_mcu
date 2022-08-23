package fpgamacro.gowin

import chisel3._
import chisel3.util._

class FLASH256K extends BlackBox {
  val DOUT = Output(UInt(32.W)) // [31:0] Output 数据输出总线。
  val DIN = Input(UInt(32.W)) // [31:0] Input 数据输入总线。
  // [6:0] Input X 地址总线，访问行地址，其中 XADR[n:3]用于选择某一页，XADR[2:0] 用于选择一页中的某一行，一页由 8 行组成，一行由 64 列组成。
  val XADR = Input(UInt(7.W))
  val YADR = Input(UInt(6.W)) // [5:0] Input Y 地址总线，用于选择一行存储单元中的某一列，一行由 64 列组成。
  val XE = Input(Bool()) // Input X 地址使能信号，当 XE 为 0 时，所有行地址均不使能。
  val YE = Input(Bool()) // Input Y 地址使能信号，当 YE 为 0 时，所有列地址均不使能。
  val SE = Input(Bool()) // Input 检测放大器使能信号，高电平有效。
  val PROG = Input(Bool()) // Input 写信号，高电平有效。
  val ERASE = Input(Bool()) // Input 擦除信号，高电平有效。
  val NVSTR = Input(Bool()) // Input Flash 数据存储信号，高电平有效。
}
