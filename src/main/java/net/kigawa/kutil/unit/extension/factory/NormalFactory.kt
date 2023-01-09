package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.api.extention.UnitFactory
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.exception.UnitException
import java.lang.reflect.Constructor

@LateInit
class NormalFactory(
  private val injectorComponent: UnitInjectorComponent,
): UnitFactory {
  
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    val constructor = getConstructor(identify.unitClass)
    val parameters = injectorComponent.findUnits(constructor, stack).toTypedArray()
    @Suppress("UNCHECKED_CAST")
    return constructor.newInstance(*parameters) as T
  }
  
  companion object {
    @JvmStatic
    fun getConstructor(unitClass: Class<*>): Constructor<*> {
      val constructors = unitClass.constructors
      if (constructors.size == 1) return constructors[0]
      for (constructor in constructors) {
        if (constructor.isAnnotationPresent(Inject::class.java)) {
          return constructor
        }
      }
      throw UnitException("could not get constructor", unitClass)
    }
  }
}