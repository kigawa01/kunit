package net.kigawa.kutil.unit.extension.closer

import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitCloser {
  fun closeUnit(identify: UnitIdentify<Any>): Boolean
}