package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.extention.InitializedFilter

interface InitializedFilterComponent: ComponentHolder<InitializedFilter> {
  fun <T: Any> filter(obj: T, stack: InitStack): T
}