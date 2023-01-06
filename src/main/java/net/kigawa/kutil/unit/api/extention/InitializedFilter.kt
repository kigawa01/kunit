package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.component.InitStack

interface InitializedFilter {
  fun <T: Any> filter(obj: T, stack: InitStack): T
}