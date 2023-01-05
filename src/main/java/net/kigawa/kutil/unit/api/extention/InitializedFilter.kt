package net.kigawa.kutil.unit.api.extention

interface InitializedFilter {
  fun <T> filter(obj: T): T
}