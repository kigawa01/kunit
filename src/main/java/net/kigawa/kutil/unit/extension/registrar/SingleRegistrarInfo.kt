package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class SingleRegistrarInfo(identify: UnitIdentify<*>): AbstractRegistrarInfo() {
  init {
    identifies.add(identify)
  }
}