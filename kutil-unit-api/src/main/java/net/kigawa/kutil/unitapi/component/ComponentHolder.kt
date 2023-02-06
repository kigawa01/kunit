package net.kigawa.kutil.unitapi.component

interface ComponentHolder<T: Any> {
  fun add(clazz: Class<out T>)
  fun remove(clazz: Class<out T>)
}