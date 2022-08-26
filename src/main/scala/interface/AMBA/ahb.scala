package arm.amba3

import chisel3._
import chisel3.util._

class AHB_SRAM extends Bundle {
  val addr = Output(UInt(13.W))
  val cs = Output(Bool())
  val rdata = Input(UInt(32.W))
  val wdata = Output(UInt(32.W))
  val wren = Output(UInt(4.W))
}

class AHB extends Bundle {
  val EXREQ = Output(Bool())
  val EXRESP = Input(Bool())
  val HADDR = Input(UInt(32.W))
  val HAUSER = Output(Bool())
  val HBURST = Output(UInt(3.W))
  val HMASTER = Output(UInt(4.W))
  val HMASTLOCK = Output(Bool())
  val HPROT = Output(UInt(4.W))
  val HRDATA = Input(UInt(32.W))
  val HREADYMUX = Output(Bool())
  val HREADYOUT = Output(Bool())
  val HRESP = Input(Bool())
  val HRUSER = Input(UInt(3.W))
  val HSEL = Input(Bool())
  val HSIZE = Output(UInt(3.W))
  val HTRANS = Output(UInt(2.W))
  val HWDATA = Output(UInt(32.W))
  val HWRITE = Output(Bool())
  val HWUSER = Output(UInt(4.W))
  val MEMATTR = Output(UInt(2.W))
}

class AHB_FLASH extends Bundle {
  val error = Input(Bool()) // FLASHERR
  val interrupt = Input(Bool()) // FLASHINT

  // TARGFLASH0EXRESP
  val EXRESP = Input(Bool()) // TARGFLASH0, EXRESP
  // TARGFLASH0HADDR
  val HADDR = Output(UInt(29.W)) // TARGFLASH0, HADDR
  // TARGFLASH0HBURST
  val HBURST = Output(UInt(3.W)) // TARGFLASH0, HBURST
  // TARGFLASH0HRDATA
  val HRDATA = Input(UInt(32.W)) // TARGFLASH0, HRDATA
  // TARGFLASH0HREADYMUX
  val HREADYMUX = Output(Bool()) // TARGFLASH0, HREADYOUT
  // TARGFLASH0HREADYOUT
  val HREADYOUT = Input(Bool()) // TARGFLASH0, EXRESP
  // TARGFLASH0HRESP
  val HRESP = Input(Bool()) // TARGFLASH0, HRESP
  // TARGFLASH0HRUSER
  val HRUSER = Input(UInt(3.W)) // TARGFLASH0, HRUSER
  // TARGFLASH0HSEL
  val HSEL = Output(Bool()) // TARGFLASH0, HSELx
  // TARGFLASH0HSIZE
  val HSIZE = Output(UInt(3.W)) // TARGFLASH0, HSIZE
  // TARGFLASH0HTRANS
  val HTRANS = Output(UInt(2.W)) // TARGFLASH0, HTRANS
}
