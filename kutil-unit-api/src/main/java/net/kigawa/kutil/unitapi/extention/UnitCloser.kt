package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.UnitInfo

interface UnitCloser {
  fun closeUnit(info: UnitInfo<out Any>): Boolean
}