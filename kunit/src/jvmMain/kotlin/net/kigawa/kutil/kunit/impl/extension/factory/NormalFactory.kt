package net.kigawa.kutil.kunit.impl.extension.factory

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.Inject
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.exception.UnitConstructorException
import net.kigawa.kutil.kunit.api.exception.UnitFactoryException
import net.kigawa.kutil.kunit.api.extention.UnitFactory
import net.kigawa.kutil.kunit.api.options.FindInitGetOption
import net.kigawa.kutil.kunit.api.options.FindOptions
import java.lang.reflect.Constructor

@LateInit
class NormalFactory(
  private val container: UnitContainer,
) : UnitFactory {

  override fun <T : Any> init(identify: UnitIdentify<T>, initStack: InitStack): T {
    val constructor = getConstructor(identify.unitClass)
    val parameters = UnitIdentify.createList(constructor)
      .map { container.getUnit(it, FindOptions(FindInitGetOption(initStack))) }
      .toTypedArray()
    try {
      @Suppress("UNCHECKED_CAST")
      return constructor.newInstance(*parameters) as T
    } catch (e: Exception) {
      throw UnitFactoryException("unit could not initialize", e, identify)
    }
  }

  companion object {
    @JvmStatic
    fun getConstructor(unitClass: Class<*>): Constructor<*> {
      val constructors = unitClass.constructors
      if (constructors.size == 1) return constructors[0]
      for (constructor in constructors) {
        if (constructor.isAnnotationPresent(net.kigawa.kutil.kunit.api.annotation.Inject::class.java)) {
          return constructor
        }
      }
      throw UnitConstructorException("could not get constructor", unitClass = unitClass)
    }
  }
}