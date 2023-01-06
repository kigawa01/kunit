package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.UnitCloser

interface UnitCloserComponent: ComponentHolder<UnitCloser> {
  fun closeUnit(identify: UnitInfo<out Any>)
}