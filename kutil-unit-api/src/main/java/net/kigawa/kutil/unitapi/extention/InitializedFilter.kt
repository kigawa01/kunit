package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.component.InitStack

interface InitializedFilter {
  fun <T: Any> filter(obj: T, stack: InitStack): T
}