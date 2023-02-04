package basic

import chisel3._
import chisel3.util._

class LedBlink(count_max: UInt = 27000000.U) extends Module {
  val led = IO(Output(Bool()))
  val led_reg = RegInit(0.U(1.W))
  led := led_reg

  val count = RegInit(0.U(32.W))
  when(count === count_max) {
    led_reg := ~led_reg
    count := 0.U
  }.otherwise {
    count := count + 1.U
  }
}
