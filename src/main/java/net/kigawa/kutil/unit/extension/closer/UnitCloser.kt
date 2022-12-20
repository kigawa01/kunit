package net.kigawa.kutil.unit.extension.closer

import net.kigawa.kutil.unit.component.info.UnitInfo

interface UnitCloser {
  fun closeUnit(identify: UnitInfo<out Any>): Boolean
}