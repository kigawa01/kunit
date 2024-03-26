package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.extention.PreCloseFilter

interface PreCloseFilterComponent: ComponentHolder<PreCloseFilter> {
  fun filter(info: UnitInfo<out Any>)
}