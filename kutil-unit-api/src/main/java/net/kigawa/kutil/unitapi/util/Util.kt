package net.kigawa.kutil.unitapi.util

object Util {
  fun <T> connectList(vararg list: List<T>): List<T> {
    return list.flatMap {it}
  }
}