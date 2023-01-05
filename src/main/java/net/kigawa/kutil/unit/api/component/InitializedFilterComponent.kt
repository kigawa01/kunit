package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.api.extention.InitializedFilter

interface InitializedFilterComponent: ComponentHolder<InitializedFilter> {
  fun <T> filter(obj: T): T
}