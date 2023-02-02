package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.UnitCloser

interface UnitCloserComponent: ComponentHolder<UnitCloser> {
  fun closeUnit(identify: UnitInfo<out Any>)
}