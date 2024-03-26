package net.kigawa.kutil.kunit.impl.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.*
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.exception.CircularReferenceException
import net.kigawa.kutil.kunit.api.exception.NoFoundFactoryException
import net.kigawa.kutil.kunit.api.extention.*
import net.kigawa.kutil.kunit.impl.extension.factory.NormalFactory

@LateInit
class UnitFactoryComponentImpl(
  container: UnitContainer,
  private val database: ComponentDatabase,
  private val initializedFilter: InitializedFilterComponent,
  private val preInitFilterComponent: PreInitFilterComponent,
) : UnitFactoryComponent,
    net.kigawa.kutil.kunit.impl.component.ComponentHolderImpl<UnitFactory>(container, database) {
  init {
    addFactory(NormalFactory(container))
  }

  @Throws(CircularReferenceException::class)
  override fun <T : Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    val initStack = try {
      stack.addIdentify(identify)
    } catch (e: CircularReferenceException) {

      throw e
    }

    preInitFilterComponent.filter(identify, initStack)

    val result = lastMap {
      it.init(identify, initStack)
    } ?: throw NoFoundFactoryException("valid factory is not found", identify = identify)

    return initializedFilter.filter(result, initStack)
  }

  fun addFactory(factory: UnitFactory) {
    database.registerComponent(factory, null)
    classes.add(factory.javaClass)
  }
}