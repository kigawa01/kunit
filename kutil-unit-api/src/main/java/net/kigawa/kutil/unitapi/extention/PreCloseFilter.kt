package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.UnitInfo

interface PreCloseFilter {
  fun filter(info: UnitInfo<out Any>)
}