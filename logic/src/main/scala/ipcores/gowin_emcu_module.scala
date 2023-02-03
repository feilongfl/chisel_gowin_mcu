package gowin.ips

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

import pio.{PIO_IIC, PIO_Uart, PIO_GPIO, SRAM}

class SP extends BlackBox {
  val io = IO(new Bundle {
// DO[31:0]/DO[35:0] Output 数据输出信号
    val DO = Output(UInt(32.W));
// DI[31:0]/DI[35:0] Input 数据输入信号
    val DI = Input(UInt(32.W));
// AD[13:0] Input 地址输入信号
    val AD = Input(UInt(14.W));
// WRE Input
    val WRE = Input(UInt(1.W));
// 写使能输入信号
// 1：写入
// 0：读出
// CE Input 时钟使能输入信号，高电平有效
    val CE = Input(UInt(1.W));
// CLK Input 时钟输入信号
    val CLK = Input(Clock());
// RESET Input
    val RESET = Input(Reset());
// 复位输入信号，支持同步复位和异步复位，高
// 电平有效。RESET 复位寄存器，而不是复位存
// 储器内的值
// OCE Input 输出时钟使能信号，用于 pipline 模式，对
// bypass 模式无效
// BLKSEL[2:0] Input BSRAM块选择信号, 用于需要多个BSRAM存
// 储单元级联实现容量扩展
  })
}

class GOWIN_SP extends Module {
  val sp = Module(new SP())

  sp.io.CLK := clock
  sp.io.RESET := reset
  sp.io.CE := 1.U(1.W)

  val sram = IO(new SRAM())
}

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
    val SRAM0RDATA = Input(UInt(13.W));
    // SRAM0ADDR[12:0] output SRAM address
    val SRAM0ADDR = Output(UInt(13.W));
    // SRAM0WREN[3:0] output SRAM Byte write enable
    val SRAM0WREN = Output(UInt(4.W));
    // SRAM0WDATA[31:0] output SRAM Write data
    // SRAM0CS output SRAM Chip select

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
    // MTXREMAP[3:0] input The MTXREMAP signals control the remapping of the boot memory range.
    // FLASHERR input Output clock, used by the TPA to sample the other pins
    // FLASHINT input Output clock, used by the TPA to sample the other pins
    // TARGFLASH0HRDATA[31:0] input TARGFLASH0, HRDATA
    // TARGFLASH0HRUSER[2:0] input TARGFLASH0, HRUSER
    // TARGFLASH0HRESP input TARGFLASH0, HRESP
    // TARGFLASH0EXRESP input TARGFLASH0, EXRESP
    // TARGFLASH0HREADYOUT input TARGFLASH0, EXRESP
    // TARGFLASH0HSEL output TARGFLASH0, HSELx
    // TARGFLASH0HADDR[28:0] output TARGFLASH0, HADDR
    // TARGFLASH0HTRANS[1:0] output TARGFLASH0, HTRANS
    // TARGFLASH0HSIZE[2:0] output TARGFLASH0, HSIZE
    // TARGFLASH0HBURST[2:0] output TARGFLASH0, HBURST
    // TARGFLASH0HREADYMUX output TARGFLASH0, HREADYOUT

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

class Gowin_EMPU_Module(gpio_width: Int = 16) extends Module {
  val emcu = Module(new EMCU())

  emcu.io.FCLK := clock
  // emcu.io.RTCSRCCLK := clock
  emcu.io.PORESETN := reset
  emcu.io.SYSRESETN := reset

  val peripherals = IO(new Bundle {
    val gpio = new PIO_GPIO(gpio_width)
    // val iic = new PIO_IIC()
    // val uart0 = new PIO_Uart()
  })
  peripherals.gpio.in <> emcu.io.IOEXPINPUTI
  peripherals.gpio.out <> emcu.io.IOEXPOUTPUTO
  peripherals.gpio.outen <> emcu.io.IOEXPOUTPUTENO

  // peripherals.iic.clock <> emcu.io.scl
  // peripherals.iic.data <> emcu.io.sda

  // peripherals.uart0.tx <> emcu.io.uart0_txd
  // peripherals.uart0.rx <> emcu.io.uart0_rxd
}

class Gowin_EMPU(freq: UInt = 27000000.U) extends Module {
  val gpio = IO(new PIO_GPIO(16))
  // val iic = IO(new PIO_IIC())
  // val uart = IO(new PIO_Uart())
  val mcu = Module(new Gowin_EMPU_Module())

  gpio <> mcu.peripherals.gpio
  // iic <> mcu.peripherals.iic
  // uart <> mcu.peripherals.uart0
}
