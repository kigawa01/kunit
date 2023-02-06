package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.extention.InitializedFilter

interface InitializedFilterComponent: ComponentHolder<InitializedFilter> {
  fun <T: Any> filter(obj: T, stack: InitStack): T
}