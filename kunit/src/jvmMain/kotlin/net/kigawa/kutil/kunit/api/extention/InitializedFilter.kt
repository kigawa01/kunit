package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.component.InitStack

interface InitializedFilter {
  fun <T: Any> filter(obj: T, stack: InitStack): T
}