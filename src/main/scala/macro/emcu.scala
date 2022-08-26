package fpgamacro.gowin

import chisel3._
import chisel3.util._

import arm.amba3._
import gowin.cpu.peripherals._

class EMCU extends BlackBox {
  val io = IO(new Bundle {
    val FCLK = Input(Clock()) // Free running clock
    val PORESETN = Input(Bool()) // Power on reset
    val SYSRESETN = Input(Bool()) // System reset
    val RTCSRCCLK = Input(Clock()) // Used to generate RTC clock
    val IOEXPINPUTI = Input(UInt(16.W)) // IOEXPINPUTI
    val UART0RXDI = Input(Bool()) // UART0RXDI
    val UART1RXDI = Input(Bool()) // UART1RXDI
    val SRAM0RDATA = Input(UInt(32.W)) // SRAM Read data bus
    val TARGFLASH0HRDATA = Input(UInt(32.W)) // TARGFLASH0, HRDATA
    val TARGFLASH0HRUSER = Input(UInt(3.W)) // TARGFLASH0, HRUSER
    val TARGFLASH0HRESP = Input(Bool()) // TARGFLASH0, HRESP
    val TARGFLASH0EXRESP = Input(Bool()) // TARGFLASH0, EXRESP
    val TARGFLASH0HREADYOUT = Input(Bool()) // TARGFLASH0, EXRESP
    val TARGEXP0HRDATA = Input(UInt(32.W)) // TARGEXP0, HRDATA
    val TARGEXP0HREADYOUT = Input(Bool()) // TARGEXP0, HREADY
    val TARGEXP0HRESP = Input(Bool()) // TARGEXP0, HRESP
    val TARGEXP0EXRESP = Input(Bool()) // TARGEXP0, EXRESP
    val TARGEXP0HRUSER = Input(UInt(3.W)) // TARGEXP0, HRUSER
    val INITEXP0HSEL = Input(Bool()) // INITEXP0, HSELx
    val INITEXP0HADDR = Input(UInt(32.W)) // INITEXP0, HADDR
    val INITEXP0HTRANS = Input(UInt(2.W)) // INITEXP0, HTRANS
    val INITEXP0HWRITE = Input(Bool()) // INITEXP0, HWRITE
    val INITEXP0HSIZE = Input(UInt(3.W)) // INITEXP0, HSIZE
    val INITEXP0HBURST = Input(UInt(3.W)) // INITEXP0, HBURST
    val INITEXP0HPROT = Input(UInt(4.W)) // INITEXP0, HPROT
    val INITEXP0MEMATTR = Input(UInt(2.W)) // INITEXP0, MEMATTR
    val INITEXP0EXREQ = Input(Bool()) // INITEXP0, EXREQ
    val INITEXP0HMASTER = Input(UInt(4.W)) // INITEXP0, HMASTER
    val INITEXP0HWDATA = Input(UInt(32.W)) // INITEXP0, HWDATA
    val INITEXP0HMASTLOCK = Input(Bool()) // INITEXP0, HMASTLOCK
    val INITEXP0HAUSER = Input(Bool()) // INITEXP0, HAUSER
    val INITEXP0HWUSER = Input(UInt(4.W)) // INITEXP0, HWUSER
    val APBTARGEXP2PRDATA = Input(UInt(32.W)) // APBTARGEXP2, PRDATA
    val APBTARGEXP2PREADY = Input(Bool()) // APBTARGEXP2, PREADY
    val APBTARGEXP2PSLVERR = Input(Bool()) // APBTARGEXP2, PSLVERR
    val MTXREMAP = Input(
      UInt(4.W)
    ) // The MTXREMAP signals control the remapping of the boot memory range.
    val DAPSWDITMS = Input(Bool()) // Debug TMS
    val DAPTDI = Input(Bool()) // Debug TDI
    val DAPNTRST = Input(Bool()) // Test reset
    val DAPSWCLKTCK = Input(Clock()) // Test clock / SWCLK
    val FLASHERR =
      Input(Bool()) // Output clock, used by the TPA to sample the other pins

    val FLASHINT =
      Input(Bool()) // Output clock, used by the TPA to sample the other pins
    val GPINT = Input(UInt(5.W)) // GPINT
    val IOEXPOUTPUTO = Output(UInt(16.W)) // IOEXPOUTPUTO
    val IOEXPOUTPUTENO = Output(UInt(16.W)) // IOEXPOUTPUTENO
    val UART0TXDO = Output(Bool()) // UART0TXDO
    val UART1TXDO = Output(Bool()) // UART1TXDO
    val UART0BAUDTICK = Output(Clock()) // UART0BAUDTICK
    val UART1BAUDTICK = Output(Clock()) // UART1BAUDTICK
    val INTMONITOR = Output(Bool()) // INTMONITOR
    val MTXHRESETN = Output(Bool()) // SRAM/Flash Chip reset
    val SRAM0ADDR = Output(UInt(13.W)) // SRAM address
    val SRAM0WREN = Output(UInt(4.W)) // SRAM Byte write enable
    val SRAM0WDATA = Output(UInt(32.W)) // SRAM Write data
    val SRAM0CS = Output(Bool()) // SRAM Chip select
    val TARGFLASH0HSEL = Output(Bool()) // TARGFLASH0, HSELx
    val TARGFLASH0HADDR = Output(UInt(29.W)) // TARGFLASH0, HADDR
    val TARGFLASH0HTRANS = Output(UInt(2.W)) // TARGFLASH0, HTRANS
    val TARGFLASH0HSIZE = Output(UInt(3.W)) // TARGFLASH0, HSIZE
    val TARGFLASH0HBURST = Output(UInt(3.W)) // TARGFLASH0, HBURST
    val TARGFLASH0HREADYMUX = Output(Bool()) // TARGFLASH0, HREADYOUT
    val TARGEXP0HSEL = Output(Bool()) // TARGEXP0, HSELx
    val TARGEXP0HADDR = Output(UInt(32.W)) // TARGEXP0, HADDR
    val TARGEXP0HTRANS = Output(UInt(2.W)) // TARGEXP0, HTRANS
    val TARGEXP0HWRITE = Output(Bool()) // TARGEXP0, HWRITE
    val TARGEXP0HSIZE = Output(UInt(3.W)) // TARGEXP0, HSIZE
    val TARGEXP0HBURST = Output(UInt(3.W)) // TARGEXP0, HBURST
    val TARGEXP0HPROT = Output(UInt(4.W)) // TARGEXP0, HPROT
    val TARGEXP0MEMATTR = Output(UInt(2.W)) // TARGEXP0, MEMATTR
    val TARGEXP0EXREQ = Output(Bool()) // TARGEXP0, EXREQ
    val TARGEXP0HMASTER = Output(UInt(4.W)) // TARGEXP0, HMASTER
    val TARGEXP0HWDATA = Output(UInt(32.W)) // TARGEXP0, HWDATA
    val TARGEXP0HMASTLOCK = Output(Bool()) // TARGEXP0, HMASTLOCK
    val TARGEXP0HREADYMUX = Output(Bool()) // TARGEXP0, HREADYOUT
    val TARGEXP0HAUSER = Output(Bool()) // TARGEXP0, HAUSER
    val TARGEXP0HWUSER = Output(UInt(4.W)) // TARGEXP0, HWUSER
    val INITEXP0HRDATA = Output(UInt(32.W)) // INITEXP0, HRDATA
    val INITEXP0HREADY = Output(Bool()) // INITEXP0, HREADY
    val INITEXP0HRESP = Output(Bool()) // INITEXP0, HRESP
    val INITEXP0EXRESP = Output(Bool()) // INITEXP0,EXRESP
    val INITEXP0HRUSER = Output(UInt(3.W)) // INITEXP0, HRUSER

    val APBTARGEXP2PSTRB = Output(UInt(4.W)) // APBTARGEXP2, PSTRB
    val APBTARGEXP2PPROT = Output(UInt(3.W)) // APBTARGEXP2, PPROT
    val APBTARGEXP2PSEL = Output(Bool()) // APBTARGEXP2, PSELx
    val APBTARGEXP2PENABLE = Output(Bool()) // APBTARGEXP2, PENABLE
    val APBTARGEXP2PADDR = Output(UInt(12.W)) // APBTARGEXP2, PADDR
    val APBTARGEXP2PWRITE = Output(Bool()) // APBTARGEXP2, PWRITE
    val APBTARGEXP2PWDATA = Output(UInt(32.W)) // APBTARGEXP2, PWDATA
    val DAPTDO = Output(Bool()) // Debug TDO
    val DAPJTAGNSW =
      Output(Bool()) // JTAG or Serial-Wire selection JTAG mode(1) or SW mode(0)
    val DAPNTDOEN = Output(Bool()) // TDO output pad control signal
    val TPIUTRACEDATA = Output(UInt(4.W)) // Output data
    val TPIUTRACECLK =
      Output(Clock()) // Output clock, used by the TPA to sample the other pins
  })
}

// class unknow extends Bundle {
// MTXHRESETN
// MTXREMAP
// }

class EmcuModule extends Module {
  val io = IO(new Bundle {
    val rtc_clk = Input(Clock()) // Fast clock
    // val dap = new DAP() // debug port
    // val tpiu = new TPIU()

    // val interrupt = Input(UInt(5.W)) // GPINT
    // val interrupt_moni = Output(Bool()) // IntMonitor

    // val ahb_master = new AHB() // TARGEXP0
    // val ahb_slave = Flipped(new AHB()) // INTEXP0
    // val apb_master = new APB()

    // val sram0 = new SRAM()
    val flash0 = new AHB_FLASH()

    val gpio = new GPIO() // gpio

    // val uart0 = new UART() // uart
    // val uart1 = new UART() // uart
  })

  val mcu = Module(new EMCU())

  // clock and reset
  mcu.io.FCLK := clock
  mcu.io.RTCSRCCLK := io.rtc_clk
  mcu.io.PORESETN := reset
  mcu.io.SYSRESETN := reset

  // INTERRUPT
  // mcu.io.GPINT := io.interrupt

  // DAP
  // io.dap.sw := mcu.io.DAPJTAGNSW
  // io.dap.tdo_en := mcu.io.DAPNTDOEN
  // io.dap.tdo := mcu.io.DAPTDO
  // mcu.io.DAPSWDITMS := io.dap.tms
  // mcu.io.DAPTDI := io.dap.tdi
  // mcu.io.DAPNTRST := io.dap.trst
  // mcu.io.DAPSWCLKTCK := io.dap.tclk

  // SRAM
  // io.sram0.addr := mcu.io.SRAM0ADDR
  // io.sram0.wren := mcu.io.SRAM0WREN
  // io.sram0.wdata := mcu.io.SRAM0WDATA
  // io.sram0.cs := mcu.io.SRAM0CS
  // mcu.io.SRAM0RDATA := io.sram0.rdata

  // flash
  mcu.io.TARGFLASH0HRDATA := io.flash0.HRDATA
  mcu.io.TARGFLASH0HRUSER <> io.flash0.HRUSER
  mcu.io.TARGFLASH0HRESP <> io.flash0.HRESP
  mcu.io.TARGFLASH0EXRESP <> io.flash0.EXRESP
  mcu.io.TARGFLASH0HREADYOUT <> io.flash0.HREADYOUT
  mcu.io.FLASHERR <> io.flash0.error
  mcu.io.FLASHINT <> io.flash0.interrupt
  io.flash0.HSEL := mcu.io.TARGFLASH0HSEL
  io.flash0.HADDR := mcu.io.TARGFLASH0HADDR
  io.flash0.HTRANS := mcu.io.TARGFLASH0HTRANS
  io.flash0.HSIZE := mcu.io.TARGFLASH0HSIZE
  io.flash0.HBURST := mcu.io.TARGFLASH0HBURST
  io.flash0.HREADYMUX := mcu.io.TARGFLASH0HREADYMUX

  // GPIO
  mcu.io.IOEXPINPUTI := io.gpio.input
  io.gpio.output := mcu.io.IOEXPOUTPUTO
  io.gpio.output_enable := mcu.io.IOEXPOUTPUTENO

  // UART
  // mcu.io.UART0RXDI := io.uart0.rx
  // io.uart0.tx := mcu.io.UART0TXDO
  // io.uart0.tick := mcu.io.UART0BAUDTICK

  // mcu.io.UART1RXDI := io.uart1.rx
  // io.uart1.tx := mcu.io.UART1TXDO
  // io.uart1.tick := mcu.io.UART1BAUDTICK

  // FCLK <> DontCare
  // PORESETN <> DontCare
  // SYSRESETN <> DontCare
  // RTCSRCCLK <> DontCare
  // mcu.io.IOEXPINPUTI <> DontCare
  mcu.io.UART0RXDI <> DontCare
  mcu.io.UART1RXDI <> DontCare
  // mcu.io.SRAM0RDATA <> DontCare
  // mcu.io.TARGFLASH0HRDATA <> DontCare
  // mcu.io.TARGFLASH0HRUSER <> DontCare
  // mcu.io.TARGFLASH0HRESP <> DontCare
  // mcu.io.TARGFLASH0EXRESP <> DontCare
  // mcu.io.TARGFLASH0HREADYOUT <> DontCare
  mcu.io.TARGEXP0HRDATA <> DontCare
  mcu.io.TARGEXP0HREADYOUT <> DontCare
  mcu.io.TARGEXP0HRESP <> DontCare
  mcu.io.TARGEXP0EXRESP <> DontCare
  mcu.io.TARGEXP0HRUSER <> DontCare
  mcu.io.INITEXP0HSEL <> DontCare
  mcu.io.INITEXP0HADDR <> DontCare
  mcu.io.INITEXP0HTRANS <> DontCare
  mcu.io.INITEXP0HWRITE <> DontCare
  mcu.io.INITEXP0HSIZE <> DontCare
  mcu.io.INITEXP0HBURST <> DontCare
  mcu.io.INITEXP0HPROT <> DontCare
  mcu.io.INITEXP0MEMATTR <> DontCare
  mcu.io.INITEXP0EXREQ <> DontCare
  mcu.io.INITEXP0HMASTER <> DontCare
  mcu.io.INITEXP0HWDATA <> DontCare
  mcu.io.INITEXP0HMASTLOCK <> DontCare
  mcu.io.INITEXP0HAUSER <> DontCare
  mcu.io.INITEXP0HWUSER <> DontCare
  mcu.io.APBTARGEXP2PRDATA <> DontCare
  mcu.io.APBTARGEXP2PREADY <> DontCare
  mcu.io.APBTARGEXP2PSLVERR <> DontCare
  mcu.io.MTXREMAP <> DontCare
  mcu.io.DAPSWDITMS <> DontCare
  mcu.io.DAPTDI <> DontCare
  mcu.io.DAPNTRST <> DontCare
  mcu.io.DAPSWCLKTCK <> DontCare
  // mcu.io.FLASHERR <> DontCare
  // mcu.io.FLASHINT <> DontCare
  mcu.io.GPINT <> DontCare
  // mcu.io.IOEXPOUTPUTO <> DontCare
  // mcu.io.IOEXPOUTPUTENO <> DontCare
  mcu.io.UART0TXDO <> DontCare
  mcu.io.UART1TXDO <> DontCare
  mcu.io.UART0BAUDTICK <> DontCare
  mcu.io.UART1BAUDTICK <> DontCare
  mcu.io.INTMONITOR <> DontCare
  mcu.io.MTXHRESETN <> DontCare
  mcu.io.SRAM0ADDR <> DontCare
  mcu.io.SRAM0WREN <> DontCare
  mcu.io.SRAM0WDATA <> DontCare
  mcu.io.SRAM0CS <> DontCare
  mcu.io.TARGFLASH0HSEL <> DontCare
  mcu.io.TARGFLASH0HADDR <> DontCare
  mcu.io.TARGFLASH0HTRANS <> DontCare
  mcu.io.TARGFLASH0HSIZE <> DontCare
  mcu.io.TARGFLASH0HBURST <> DontCare
  mcu.io.TARGFLASH0HREADYMUX <> DontCare
  mcu.io.TARGEXP0HSEL <> DontCare
  mcu.io.TARGEXP0HADDR <> DontCare
  mcu.io.TARGEXP0HTRANS <> DontCare
  mcu.io.TARGEXP0HWRITE <> DontCare
  mcu.io.TARGEXP0HSIZE <> DontCare
  mcu.io.TARGEXP0HBURST <> DontCare
  mcu.io.TARGEXP0HPROT <> DontCare
  mcu.io.TARGEXP0MEMATTR <> DontCare
  mcu.io.TARGEXP0EXREQ <> DontCare
  mcu.io.TARGEXP0HMASTER <> DontCare
  mcu.io.TARGEXP0HWDATA <> DontCare
  mcu.io.TARGEXP0HMASTLOCK <> DontCare
  mcu.io.TARGEXP0HREADYMUX <> DontCare
  mcu.io.TARGEXP0HAUSER <> DontCare
  mcu.io.TARGEXP0HWUSER <> DontCare
  mcu.io.INITEXP0HRDATA <> DontCare
  mcu.io.INITEXP0HREADY <> DontCare
  mcu.io.INITEXP0HRESP <> DontCare
  mcu.io.INITEXP0EXRESP <> DontCare
  mcu.io.INITEXP0HRUSER <> DontCare
  mcu.io.APBTARGEXP2PSTRB <> DontCare
  mcu.io.APBTARGEXP2PPROT <> DontCare
  mcu.io.APBTARGEXP2PSEL <> DontCare
  mcu.io.APBTARGEXP2PENABLE <> DontCare
  mcu.io.APBTARGEXP2PADDR <> DontCare
  mcu.io.APBTARGEXP2PWRITE <> DontCare
  mcu.io.APBTARGEXP2PWDATA <> DontCare
  mcu.io.DAPTDO <> DontCare
  mcu.io.DAPJTAGNSW <> DontCare
  mcu.io.DAPNTDOEN <> DontCare
  mcu.io.TPIUTRACEDATA <> DontCare
  mcu.io.TPIUTRACECLK <> DontCare

}
