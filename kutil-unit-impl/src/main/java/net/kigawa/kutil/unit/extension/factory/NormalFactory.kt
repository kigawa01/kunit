package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unitapi.UnitIdentifies
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.Inject
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.options.FindOptions
import net.kigawa.kutil.unitapi.extention.UnitFactory
import java.lang.reflect.Constructor

@LateInit
class NormalFactory(
  private val injectorComponent: UnitFinderComponent,
): UnitFactory {
  
  @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
  override fun <T: Any> init(identify: UnitIdentify<T>, stack: InitStack): T {
    val constructor = getConstructor(identify.unitClass)
    val parameters =
      injectorComponent.findUnits(UnitIdentifies.createList(constructor), stack, FindOptions()).toTypedArray()
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