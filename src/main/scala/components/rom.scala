package components.rom

import chisel3._
import chisel3.util._
import chisel3.experimental.{ChiselAnnotation, annotate}
import firrtl.annotations.MemoryArrayInitAnnotation
import firrtl.FileUtils

import arm.amba3._

// https://scastie.scala-lang.org/QmN0vvjwRda0MBdx5YLxmw
class MemROM(w: Int, contents: Seq[String]) extends Module {
  val abits = log2Ceil(contents.length)
  val io = IO(new Bundle {
    val addr = Input(UInt(abits.W))
    val data = Output(UInt(w.W))
  })

  val mem = Mem(contents.length, chiselTypeOf(io.data))
  annotate(new ChiselAnnotation {
    override def toFirrtl =
      MemoryArrayInitAnnotation(mem.toTarget, contents.map(BigInt(_, 16)))
  })
  io.data := mem.read(io.addr)
}

class SBoxROM(dw: Int, contents: Seq[String]) extends Module {
  val abits = log2Ceil(contents.length)
  val io = IO(new Bundle {
    val addr = Input(UInt(abits.W))
    val data = Output(UInt(dw.W))
  })

  val rom = Module(new MemROM(dw, contents))
  rom.io.addr := io.addr
  io.data := rom.io.data
}

class AHBROM_Logic extends Module {
  val rom_blink = Seq(
    // ROM from `software/01_minimum/build/blink.bin.txt`
    "20005000",
    "0000001d",
    "00000041",
    "00000041",
    "00000041",
    "00000041",
    "00000041",
    "f64f4e0b",
    "603070ff",
    "0200f04f",
    "73fff64f",
    "60324e08",
    "39014908",
    "6033d1fd",
    "39014906",
    "e7f6d1fd",
    "0001f100",
    "0101f101",
    "0000e7fa",
    "40010010",
    "40010004",
    "000c3500"
  )

  val flash0 = IO(Flipped(new AHB_FLASH))
  val rom = Module(new SBoxROM(32, rom_blink))

  flash0 <> DontCare
  rom.io.addr := flash0.HADDR
  flash0.HRDATA := rom.io.data
}
