package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.api.component.UnitInfo

interface UnitCloser {
  fun closeUnit(info: UnitInfo<out Any>): Boolean
}