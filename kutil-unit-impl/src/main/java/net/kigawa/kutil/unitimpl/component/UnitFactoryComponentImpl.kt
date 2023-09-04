package net.kigawa.kutil.unitimpl.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.component.container.UnitContainer
import net.kigawa.kutil.unitapi.exception.CircularReferenceException
import net.kigawa.kutil.unitapi.exception.NoFoundFactoryException
import net.kigawa.kutil.unitapi.extention.*
import net.kigawa.kutil.unitimpl.extension.factory.NormalFactory

@LateInit
class UnitFactoryComponentImpl(
  container: UnitContainer,
  loggerComponent: UnitLoggerComponent,
  private val database: ComponentDatabase,
  private val initializedFilter: InitializedFilterComponent,
  private val preInitFilterComponent: PreInitFilterComponent,
) : UnitFactoryComponent,
  ComponentHolderImpl<UnitFactory>(container, database, loggerComponent) {
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