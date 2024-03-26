package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.component.UnitInfo

interface UnitCloser {
  fun closeUnit(info: UnitInfo<out Any>): Boolean
}