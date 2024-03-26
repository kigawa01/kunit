package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.extention.UnitCloser

interface UnitCloserComponent: ComponentHolder<UnitCloser> {
  fun closeUnit(info: UnitInfo<out Any>)
}