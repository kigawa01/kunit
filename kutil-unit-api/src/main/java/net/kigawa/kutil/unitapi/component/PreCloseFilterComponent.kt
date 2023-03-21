package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.PreCloseFilter

interface PreCloseFilterComponent: ComponentHolder<PreCloseFilter> {
  fun filter(info: UnitInfo<out Any>)
}