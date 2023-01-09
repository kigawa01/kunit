package net.kigawa.kutil.unit.api.component

interface ComponentHolder<T: Any> {
  fun add(clazz: Class<out T>)
  fun remove(clazz: Class<out T>)
}