package net.kigawa.kutil.kunit.item

import net.kigawa.kutil.kunit.provider.Provider
import net.kigawa.kutil.kutil.api.list.containsIf
import kotlin.reflect.KClass

class Item<T : Any>(
  val provider: Provider<T>,
  val dependencies: Set<Item<*>>,
) {
  init {
    dependencies.forEach {
      if (it.isContainDependency(provider.instanceClass)) throw CircularItemException(provider)
    }
  }

  fun <U : Any> safeCast(clazz: KClass<U>): Item<out U>? {
    @Suppress("UNCHECKED_CAST")
    if (provider.isInstanceOf(clazz)) return this as Item<out U>
    return null
  }

  private fun isContainDependency(clazz: KClass<*>): Boolean {
    if (clazz == provider.instanceClass) return true
    return dependencies.containsIf {
      it.isContainDependency(clazz)
    }
  }
}