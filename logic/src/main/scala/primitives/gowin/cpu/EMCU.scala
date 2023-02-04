package primitives.gowin.cpu

import chisel3._
import chisel3.util._

class EMCU() extends BlackBox {
  val io = IO(new Bundle {

    // ** CLOCK & RESET
    // FCLK input Free running clock
    val FCLK = Input(Clock())
    // PORESETN input Power on reset
    val PORESETN = Input(Reset())
    // SYSRESETN input System reset
    val SYSRESETN = Input(Reset())
    // RTCSRCCLK input Used to generate RTC clock
    // val RTCSRCCLK = Input(Clock())
    // INTMONITOR output INTMONITOR

    // NVIC
    // GPINT[5:0] input GPINT

    // ** SRAM
    // SRAM0RDATA[31:0] input SRAM Read data bus
    val SRAM0RDATA = Input(UInt(32.W));
    // SRAM0ADDR[12:0] output SRAM address
    val SRAM0ADDR = Output(UInt(13.W));
    // SRAM0WREN[3:0] output SRAM Byte write enable
    val SRAM0WREN = Output(UInt(4.W));
    // SRAM0WDATA[31:0] output SRAM Write data
    val SRAM0WDATA = Output(UInt(32.W));
    // SRAM0CS output SRAM Chip select
    val SRAM0CS = Output(UInt(1.W));

    // ** GPIO
    // IOEXPINPUTI[15:0] input IOEXPINPUTI
    val IOEXPINPUTI = Input(UInt(16.W)) // GPIO IN
    // IOEXPOUTPUTO[15:0] output IOEXPOUTPUTO
    val IOEXPOUTPUTO = Output(UInt(16.W)) // GPIO OUT
    // IOEXPOUTPUTENO[15:0] output IOEXPOUTPUTENO
    val IOEXPOUTPUTENO = Output(UInt(16.W)) // GPIO ENABLE

    // ** UART
    // UART0RXDI input UART0RXDI
    // UART0TXDO output UART0TXDO
    // UART0BAUDTICK output UART0BAUDTICK
    // UART1RXDI input UART1RXDI
    // UART1TXDO output UART1TXDO
    // UART1BAUDTICK output UART1BAUDTICK

    // ** FLASH
    // MTXHRESETN output SRAM/Flash Chip reset
    val MTXHRESETN = Output(UInt(1.W))
    // MTXREMAP[3:0] input The MTXREMAP signals control the remapping of the boot memory range.
    // FLASHERR input Output clock, used by the TPA to sample the other pins
    val FLASHERR = Input(UInt(1.W))
    // FLASHINT input Output clock, used by the TPA to sample the other pins
    val FLASHINT = Input(UInt(1.W))
    // TARGFLASH0HRDATA[31:0] input TARGFLASH0, HRDATA
    val TARGFLASH0HRDATA = Input(UInt(32.W))
    // TARGFLASH0HRUSER[2:0] input TARGFLASH0, HRUSER
    val TARGFLASH0HRUSER = Input(UInt(3.W))
    // TARGFLASH0HRESP input TARGFLASH0, HRESP
    val TARGFLASH0HRESP = Input(UInt(1.W))
    // TARGFLASH0EXRESP input TARGFLASH0, EXRESP
    val TARGFLASH0EXRESP = Input(UInt(1.W))
    // TARGFLASH0HREADYOUT input TARGFLASH0, EXRESP
    val TARGFLASH0HREADYOUT = Input(UInt(1.W))
    // TARGFLASH0HSEL output TARGFLASH0, HSELx
    val TARGFLASH0HSEL = Output(UInt(1.W))
    // TARGFLASH0HADDR[28:0] output TARGFLASH0, HADDR
    val TARGFLASH0HADDR = Output(UInt(29.W))
    // TARGFLASH0HTRANS[1:0] output TARGFLASH0, HTRANS
    val TARGFLASH0HTRANS = Output(UInt(2.W))
    // TARGFLASH0HSIZE[2:0] output TARGFLASH0, HSIZE
    val TARGFLASH0HSIZE = Output(UInt(3.W))
    // TARGFLASH0HBURST[2:0] output TARGFLASH0, HBURST
    val TARGFLASH0HBURST = Output(UInt(3.W))
    // TARGFLASH0HREADYMUX output TARGFLASH0, HREADYOUT
    val TARGFLASH0HREADYMUX = Output(UInt(1.W))

    // AHB
    // TARGEXP0HRDATA[31:0] input TARGEXP0, HRDATA
    // TARGEXP0HREADYOUT input TARGEXP0, HREADY
    // TARGEXP0HRESP input TARGEXP0, HRESP
    // TARGEXP0EXRESP input TARGEXP0, EXRESP
    // TARGEXP0HRUSER[2:0] input TARGEXP0, HRUSER
    // TARGEXP0HSEL output TARGEXP0, HSELx
    // TARGEXP0HADDR[31:0] output TARGEXP0, HADDR
    // TARGEXP0HTRANS[1:0] output TARGEXP0, HTRANS
    // TARGEXP0HWRITE output TARGEXP0, HWRITE
    // TARGEXP0HSIZE[2:0] output TARGEXP0, HSIZE
    // TARGEXP0HBURST[2:0] output TARGEXP0, HBURST
    // TARGEXP0HPROT[3:0] output TARGEXP0, HPROT
    // TARGEXP0MEMATTR[1:0] output TARGEXP0, MEMATTR
    // TARGEXP0EXREQ output TARGEXP0, EXREQ
    // TARGEXP0HMASTER[3:0] output TARGEXP0, HMASTER
    // TARGEXP0HWDATA[31:0] output TARGEXP0, HWDATA
    // TARGEXP0HMASTLOCK output TARGEXP0, HMASTLOCK
    // TARGEXP0HREADYMUX output TARGEXP0, HREADYOUT
    // TARGEXP0HAUSER output TARGEXP0, HAUSER
    // TARGEXP0HWUSER[3:0] output TARGEXP0, HWUSER

    // AHBLIGHT?
    // INITEXP0HSEL input INITEXP0, HSELx
    // INITEXP0HADDR[31:0] input INITEXP0, HADDR
    // INITEXP0HTRANS[1:0] input INITEXP0, HTRANS
    // INITEXP0HWRITE input INITEXP0, HWRITE
    // INITEXP0HSIZE[2:0] input INITEXP0, HSIZE
    // INITEXP0HBURST[2:0] input INITEXP0, HBURST
    // INITEXP0HPROT[3:0] input INITEXP0, HPROT
    // INITEXP0MEMATTR[1:0] input INITEXP0, MEMATTR
    // INITEXP0EXREQ input INITEXP0, EXREQ
    // INITEXP0HMASTER[3:0] input INITEXP0, HMASTER
    // INITEXP0HWDATA[31:0] input INITEXP0, HWDATA
    // INITEXP0HMASTLOCK input INITEXP0, HMASTLOCK
    // INITEXP0HAUSER input INITEXP0, HAUSER
    // INITEXP0HWUSER[3:0] input INITEXP0, HWUSER
    // INITEXP0HRDATA[31:0] output INITEXP0, HRDATA
    // INITEXP0HREADY output INITEXP0, HREADY
    // INITEXP0HRESP output INITEXP0, HRESP
    // INITEXP0EXRESP output INITEXP0,EXRESP
    // INITEXP0HRUSER[2:0] output INITEXP0, HRUSER

    // ** APB
    // APBTARGEXP2PRDATA[31:0] input APBTARGEXP2, PRDATA
    // APBTARGEXP2PREADY input APBTARGEXP2, PREADY
    // APBTARGEXP2PSLVERR input APBTARGEXP2, PSLVERR
    // APBTARGEXP2PSTRB[3:0] output APBTARGEXP2, PSTRB
    // APBTARGEXP2PPROT[2:0] output APBTARGEXP2, PPROT
    // APBTARGEXP2PSEL output APBTARGEXP2, PSELx
    // APBTARGEXP2PENABLE output APBTARGEXP2, PENABLE
    // APBTARGEXP2PADDR[11:0] output APBTARGEXP2, PADDR
    // APBTARGEXP2PWRITE output APBTARGEXP2, PWRITE
    // APBTARGEXP2PWDATA[31:0] output APBTARGEXP2, PWDATA

    // ** DEBUG
    // DAPTDO output Debug TDO
    // DAPJTAGNSW output JTAG or Serial-Wire selection JTAG mode(1) or SW mode(0)
    // DAPNTDOEN output
    // DAPSWDITMS input Debug TMS
    // DAPTDI input Debug TDI
    // DAPNTRST input Test reset
    // DAPSWCLKTCK input Test clock / SWCLK
    // TDO output pad control signal
    // TPIUTRACEDATA[3:0] output Output data
    // TPIUTRACECLK output Output clock, used by the TPA to sample the other pins
  })
}

class Gowin_EMPU_Bundle_GPIO() extends Bundle {
  val outen = Output(UInt(16.W))
  val out = Output(UInt(16.W))
  val in = Input(UInt(16.W))
}

class Gowin_EMPU_Bundle_SRAM() extends Bundle {
  // SRAM0RDATA[31:0] input SRAM Read data bus
  val data_in = Input(UInt(32.W));
  // SRAM0ADDR[12:0] output SRAM address
  val address = Output(UInt(13.W));
  // SRAM0WREN[3:0] output SRAM Byte write enable
  val write_enable = Output(UInt(4.W));
  // SRAM0WDATA[31:0] output SRAM Write data
  val data_out = Output(UInt(32.W));
  // SRAM0CS output SRAM Chip select
  val output_enable = Output(UInt(1.W));
}

class Gowin_EMPU_Bundle_FLASH() extends Bundle {
  // FLASHERR input Output clock, used by the TPA to sample the other pins
  val FLASHERR = Input(UInt(1.W))
  // FLASHINT input Output clock, used by the TPA to sample the other pins
  val FLASHINT = Input(UInt(1.W))
  // TARGFLASH0HRDATA[31:0] input TARGFLASH0, HRDATA
  val data = Input(UInt(32.W))
  // TARGFLASH0HRUSER[2:0] input TARGFLASH0, HRUSER
  val TARGFLASH0HRUSER = Input(UInt(3.W))
  // TARGFLASH0HRESP input TARGFLASH0, HRESP
  val TARGFLASH0HRESP = Input(UInt(1.W))
  // TARGFLASH0EXRESP input TARGFLASH0, EXRESP
  val TARGFLASH0EXRESP = Input(UInt(1.W))
  // TARGFLASH0HREADYOUT input TARGFLASH0, EXRESP
  val ready = Input(UInt(1.W))
  // TARGFLASH0HSEL output TARGFLASH0, HSELx
  val TARGFLASH0HSEL = Output(UInt(1.W))
  // TARGFLASH0HADDR[28:0] output TARGFLASH0, HADDR
  val address = Output(UInt(29.W))
  // TARGFLASH0HTRANS[1:0] output TARGFLASH0, HTRANS
  val TARGFLASH0HTRANS = Output(UInt(2.W))
  // TARGFLASH0HSIZE[2:0] output TARGFLASH0, HSIZE
  val TARGFLASH0HSIZE = Output(UInt(3.W))
  // TARGFLASH0HBURST[2:0] output TARGFLASH0, HBURST
  val TARGFLASH0HBURST = Output(UInt(3.W))
  // TARGFLASH0HREADYMUX output TARGFLASH0, HREADYOUT
  val TARGFLASH0HREADYMUX = Output(UInt(1.W))
}

class Gowin_EMPU_Module() extends Module {
  val emcu = Module(new EMCU())

  emcu.io.FCLK := clock
  emcu.io.PORESETN := reset
  emcu.io.SYSRESETN := reset

  val core = IO(new Bundle {
    val MTXHRESETN = Output(UInt(1.W))

    val sram = new Gowin_EMPU_Bundle_SRAM();
    val flash = new Gowin_EMPU_Bundle_FLASH();
  })
  core.MTXHRESETN <> emcu.io.MTXHRESETN

  core.sram.data_in <> emcu.io.SRAM0RDATA
  core.sram.address <> emcu.io.SRAM0ADDR
  core.sram.write_enable <> emcu.io.SRAM0WREN
  core.sram.data_out <> emcu.io.SRAM0WDATA
  core.sram.output_enable <> emcu.io.SRAM0CS

  core.flash.FLASHERR <> emcu.io.FLASHERR
  core.flash.FLASHINT <> emcu.io.FLASHINT
  core.flash.data <> emcu.io.TARGFLASH0HRDATA
  core.flash.TARGFLASH0HRUSER <> emcu.io.TARGFLASH0HRUSER
  core.flash.TARGFLASH0HRESP <> emcu.io.TARGFLASH0HRESP
  core.flash.TARGFLASH0EXRESP <> emcu.io.TARGFLASH0EXRESP
  core.flash.ready <> emcu.io.TARGFLASH0HREADYOUT
  core.flash.TARGFLASH0HSEL <> emcu.io.TARGFLASH0HSEL
  core.flash.address <> emcu.io.TARGFLASH0HADDR
  core.flash.TARGFLASH0HTRANS <> emcu.io.TARGFLASH0HTRANS
  core.flash.TARGFLASH0HSIZE <> emcu.io.TARGFLASH0HSIZE
  core.flash.TARGFLASH0HBURST <> emcu.io.TARGFLASH0HBURST
  core.flash.TARGFLASH0HREADYMUX <> emcu.io.TARGFLASH0HREADYMUX

  val peripherals = IO(new Bundle {
    val gpio = new Gowin_EMPU_Bundle_GPIO()
  })
  peripherals.gpio.in <> emcu.io.IOEXPINPUTI
  peripherals.gpio.out <> emcu.io.IOEXPOUTPUTO
  peripherals.gpio.outen <> emcu.io.IOEXPOUTPUTENO
}
