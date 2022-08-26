package arm.amba3

import chisel3._
import chisel3.util._

class APB extends Bundle {
  val RDATA = Input(UInt(32.W)) // APBTARGEXP2, PRDATA
  val READY = Input(Bool()) // APBTARGEXP2, PREADY
  val SLVERR = Input(Bool()) // APBTARGEXP2, PSLVERR
  val STRB = Output(UInt(4.W)) // APBTARGEXP2, PSTRB
  val PROT = Output(UInt(3.W)) // APBTARGEXP2, PPROT
  val SEL = Output(Bool()) // APBTARGEXP2, PSELx
  val ENABLE = Output(Bool()) // APBTARGEXP2, PENABLE
  val ADDR = Output(UInt(12.W)) // APBTARGEXP2, PADDR
  val WRITE = Output(Bool()) // APBTARGEXP2, PWRITE
  val WDATA = Output(UInt(32.W)) // APBTARGEXP2, PWDATA
}
