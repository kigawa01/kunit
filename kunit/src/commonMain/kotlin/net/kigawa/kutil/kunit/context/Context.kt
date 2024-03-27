package net.kigawa.kutil.kunit.context

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import net.kigawa.kutil.kunit.provider.InstanceProvider
import net.kigawa.kutil.kunit.provider.Provider
import kotlin.reflect.KClass

class Context {
  private val items = MutableStateFlow(
    listOf<Provider<out Any>>(
      InstanceProvider(this)
    )
  )

  fun <T : Any> findProvider(clazz: KClass<T>): List<Provider<out T>> {
    return items.value.mapNotNull { it.safeCast(clazz) }
  }

  fun register(item: Provider<out Any>) {
    items.update {
      it.toMutableList().apply { add(item) }
    }
  }
}