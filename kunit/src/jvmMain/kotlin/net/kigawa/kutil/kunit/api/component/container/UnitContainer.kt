@file:Suppress("unused")

package net.kigawa.kutil.kunit.api.component.container

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.exception.*
import java.util.*
import java.util.concurrent.*

interface UnitContainer :
  AutoCloseable,
  UnitRemovable,
  UnitListGetable,
  UnitGetOrNull,
  UnitGettable {
  companion object {
    @JvmStatic
    var implementsClass: Class<out UnitContainer>? = null

    init {
      try {
        @Suppress("UNCHECKED_CAST")
        implementsClass =
          Class.forName("net.kigawa.kutil.kunit.impl.component.UnitContainerImpl") as Class<out UnitContainer>
      } catch (_: ClassNotFoundException) {
      }
    }

    @JvmStatic
    fun create() = create(name = null)

    @JvmStatic
    fun create(vararg parent: UnitContainer): UnitContainer = create(name = null, *parent)

    @JvmStatic
    fun create(name: String?, vararg parent: UnitContainer): UnitContainer {
      val constructor = implementsClass?.getConstructor(String::class.java, Array<UnitContainer>::class.java)
        ?: throw UnitException("container class is not found")
      return constructor.newInstance(name ?: "", parent)
    }
  }

  fun <T : Any> contain(identify: UnitIdentify<T>): Boolean {
    return getUnitList(identify).isNotEmpty()
  }
}