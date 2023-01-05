package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitReflectionComponent
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.component.UnitIdentify
import java.lang.reflect.Constructor
import java.util.*

@LateInit
class NormalFactory(
  private val executorComponent: UnitReflectionComponent,
): UnitFactory {
  
  override fun <T: Any> init(identify: UnitIdentify<T>, initStack: InitStack): T {
    @Suppress("UNCHECKED_CAST")
    return executorComponent.callConstructor(getConstructor(identify.unitClass), initStack) as T
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