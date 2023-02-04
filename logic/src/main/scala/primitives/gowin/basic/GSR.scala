package primitives.gowin.basic

import chisel3._

class GSR extends BlackBox {
  val io = IO(new Bundle {
    val GSRI = Input(UInt(1.W));
  })
}

class GOWIN_GSR extends Module {
  val gsr = Module(new GSR())

  gsr.io.GSRI := reset
}
