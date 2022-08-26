package components.rom

import chisel3._
import chisel3.util._

import arm.amba3._

class AHBROM_Logic extends Module {
  val flash0 = IO(Flipped(new AHB_FLASH))

  flash0 <> DontCare
}
