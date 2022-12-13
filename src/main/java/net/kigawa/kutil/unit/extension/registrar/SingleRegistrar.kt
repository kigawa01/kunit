package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class SingleRegistrar(identify: UnitIdentify<*>): AbstractRegistrar() {
  init {
    identifies.add(identify)
  }
}