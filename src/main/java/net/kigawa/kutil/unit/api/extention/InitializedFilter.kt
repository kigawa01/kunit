package net.kigawa.kutil.unit.api.extention

interface InitializedFilter {
  fun <T: Any> filter(obj: T): T
}