package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.InitializedFilter

interface InitializedFilterComponent: ComponentHolder<InitializedFilter> {
  fun <T: Any> filter(obj: T): T
}