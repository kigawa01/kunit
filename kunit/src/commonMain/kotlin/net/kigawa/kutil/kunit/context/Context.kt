package net.kigawa.kutil.kunit.context

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import net.kigawa.kutil.kunit.item.Item
import net.kigawa.kutil.kunit.provider.InstanceProvider
import kotlin.reflect.KClass

class Context {
  private val items = MutableStateFlow(
    listOf<Item<out Any>>(
      Item(InstanceProvider(this), setOf())
    )
  )

  fun <T : Any> findItem(clazz: KClass<T>): List<Item<out T>> {
    return items.value.mapNotNull { it.safeCast(clazz) }
  }

  fun register(item: Item<out Any>) {
    items.update {
      it.toMutableList().apply { add(item) }
    }
  }
}