package gowin.ips

import chisel3._
import chisel3.util._

import primitives.gowin.cpu._
import primitives.gowin.memory.SP
import primitives.gowin.flash.FLASH256K

class Gowin_EMPU_SRAM4K extends Module {
  val emcu = IO(Flipped(new Gowin_EMPU_Bundle_SRAM()))
  val MTXHRESETN = IO(Input(UInt(1.W)))

  val sp0 = Module(new SP())
  val sp1 = Module(new SP())

  sp0.io.CLK := clock
  sp0.io.RESET := reset
  sp0.io.OCE := 1.U
  sp0.io.CE := reset.asBool

  sp1.io.CLK := clock
  sp1.io.RESET := reset
  sp1.io.OCE := 1.U
  sp1.io.CE := reset.asBool

  // connect to together
  sp0.io <> DontCare
  sp1.io <> DontCare
  emcu <> DontCare
}

class Gowin_EMPU_FLASH256K extends Module {
  val emcu = IO(Flipped(new Gowin_EMPU_Bundle_FLASH()))
  val MTXHRESETN = IO(Input(UInt(1.W)))

  val flash = Module(new FLASH256K)

  flash.io <> DontCare
  emcu <> DontCare
}

class Gowin_EMPU(freq: UInt = 27000000.U) extends Module {
  val mcu = Module(new Gowin_EMPU_Module)

  val sram = Module(new Gowin_EMPU_SRAM4K)
  mcu.core.sram <> sram.emcu
  sram.MTXHRESETN := mcu.core.MTXHRESETN

  val flash = Module(new Gowin_EMPU_FLASH256K)
  mcu.core.flash <> flash.emcu
  flash.MTXHRESETN := mcu.core.MTXHRESETN

  val gpio = IO(new Gowin_EMPU_Bundle_GPIO)
  mcu.peripherals.gpio <> gpio
}
