package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.component.UnitInfo

interface PreCloseFilter {
  fun filter(info: UnitInfo<out Any>)
}