package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.InitializedFilter
import net.kigawa.kutil.unit.component.InitStack

interface InitializedFilterComponent: ComponentHolder<InitializedFilter> {
  fun <T: Any> filter(obj: T, stack: InitStack): T
}