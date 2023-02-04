package primitives.gowin.memory

class SDPB extends BlackBox {
  val io = IO(new Bundle {
    // DO[31:0]/DO[35:0]Output数据输出信号
    val DO = Output(UInt(32.W));
    // DI[31:0]/DI[35:0]Input数据输入信号
    val DI = Input(UInt(32.W));
    // ADA[13:0]InputA 端地址输入信号
    val ADA = Input(UInt(14.W));
    // ADB[13:0]InputB 端地址输入信号
    val ADB = Input(UInt(14.W));
    // CEAInputA 端时钟使能信号，高电平有效
    val CEA = Input(UInt(1.W));
    // CEBInputB 端时钟使能信号，高电平有效
    val CEB = Input(UInt(1.W));
    // CLKAInputA 端时钟输入信号
    val CLKA = Input(Clock());
    // CLKBInputB 端时钟输入信号
    val CLKB = Input(Clock());
    // RESETAInputA 端复位输入信号，支持同步复位和异步复位，高电
    // 平有效
    val RESETA = Input(Reset());
    // RESETBInputB 端复位输入信号，支持同步复位和异步复位，高电
    // 平有效。RESETB 复位寄存器，而不是复位存储器内
    // 的值
    val RESETA = Input(Reset());
    // OCEInput输出时钟使能信号，用于 pipline 模式，对 bypass 模
    // 式无效
    val ORE = Input(UInt(1.W));
    // BLKSELA[2:0]InputBSRAM A 端口块选择信号, 用于需要多个 BSRAM
    // 存储单元级联实现容量扩展
    val BLKSELA = Input(UInt(1.W));
    // BLKSELB[2:0]InputBSRAM B 端口块选择信号, 用于需要多个 BSRAM
    // 存储单元级联实现容量扩展
    val BLKSELB = Input(UInt(1.W));
  })
}
