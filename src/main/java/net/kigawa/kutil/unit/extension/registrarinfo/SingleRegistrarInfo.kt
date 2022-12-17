package net.kigawa.kutil.unit.extension.registrarinfo

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class SingleRegistrarInfo<T>(identify: UnitIdentify<T>): AbstractRegistrarInfo<T>() {
  init {
    identifies.add(identify)
  }
}