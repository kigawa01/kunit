package net.kigawa.kutil.kunit.api.component

interface ComponentHolder<T: Any> {
  fun add(clazz: Class<out T>)
  fun remove(clazz: Class<out T>)
}