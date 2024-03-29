package pio

import chisel3._
import chisel3.util._
import chisel3.experimental.Analog

class PIO_IIC extends Bundle {
  val clock = Analog(1.W)
  val data = Analog(1.W)
}

class PIO_Uart extends Bundle {
  val tx = Output(UInt(1.W))
  val rx = Input(UInt(1.W))
}

class PIO_GPIO(width: Int = 16) extends Bundle {
  val outen = Output(UInt(width.W))
  val out = Output(UInt(width.W))
  val in = Input(UInt(width.W))
}
