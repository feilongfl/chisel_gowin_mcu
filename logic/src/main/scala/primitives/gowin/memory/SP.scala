package primitives.gowin.memory

import chisel3._
import chisel3.util._

class SP extends BlackBox {
  val io = IO(new Bundle {
    // DO[31:0]/DO[35:0] Output 数据输出信号
    val DO = Output(UInt(32.W));
    // DI[31:0]/DI[35:0] Input 数据输入信号
    val DI = Input(UInt(32.W));
    // AD[13:0] Input 地址输入信号
    val AD = Input(UInt(14.W));
    // WRE Input
    // 写使能输入信号
    // 1：写入
    // 0：读出
    val WRE = Input(UInt(1.W));
    // CE Input 时钟使能输入信号，高电平有效
    val CE = Input(UInt(1.W));
    // CLK Input 时钟输入信号
    val CLK = Input(Clock());
    // RESET Input
    // 复位输入信号，支持同步复位和异步复位，高
    // 电平有效。RESET 复位寄存器，而不是复位存
    // 储器内的值
    val RESET = Input(Reset());
    // OCE Input 输出时钟使能信号，用于 pipline 模式，对
    // bypass 模式无效
    val ORE = Input(UInt(1.W));
    // BLKSEL[2:0] Input BSRAM块选择信号, 用于需要多个BSRAM存
    // 储单元级联实现容量扩展
    val BLKSEL = Input(UInt(1.W));
  })
}

class GOWIN_SP_IO() extends Bundle {
  val data_out = Output(UInt(32.W));
  val data_in = Input(UInt(32.W));
  val address = Input(UInt(14.W));
  val write_enable = Input(UInt(1.W));
}
