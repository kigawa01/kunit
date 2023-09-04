@file:Suppress("unused")

package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.container.UnitListGetable
import net.kigawa.kutil.unitapi.component.container.UnitRemovable
import net.kigawa.kutil.unitapi.exception.*
import net.kigawa.kutil.unitapi.options.FindOptions
import java.util.*
import java.util.concurrent.*

@Suppress("DEPRECATION")
@Deprecated("use container.UnitContainer")
interface UnitContainer :
  AutoCloseable,
  UnitRemovable,
  UnitListGetable,
  net.kigawa.kutil.unitapi.component.container.UnitContainer {
  companion object {
    @JvmStatic
    var implementsClass: Class<out UnitContainer>? = null

    init {
      try {
        @Suppress("UNCHECKED_CAST")
        implementsClass =
          Class.forName("net.kigawa.kutil.unitimpl.component.UnitContainerImpl") as Class<out UnitContainer>
      } catch (_: ClassNotFoundException) {
      }
    }

    @JvmStatic
    fun create() = create(name = null)

    @JvmStatic
    fun create(vararg parent: UnitContainer): UnitContainer = create(name = null, *parent)

    @JvmStatic
    fun create(name: String?, vararg parent: UnitContainer): UnitContainer {
      val constructor = implementsClass
        ?.getConstructor(String::class.java, Array<net.kigawa.kutil.unitapi.component.container.UnitContainer>::class.java)
        ?: throw UnitException("container class is not found")
      return constructor.newInstance(name ?: "", parent)
    }
  }

  @Deprecated("not use", ReplaceWith("identifies.map {getUnit(it, findOptions)}"))
  fun <T : Any> getCorrespondingUnitList(
    identifies: List<UnitIdentify<out T>>,
    findOptions: FindOptions,
  ): List<T> {
    return identifies.map {
      getUnit(it, findOptions)
    }
  }

}