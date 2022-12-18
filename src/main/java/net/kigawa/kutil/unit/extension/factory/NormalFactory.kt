package net.kigawa.kutil.unit.extension.factory

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.component.executor.ExecutorComponent
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.lang.reflect.Constructor
import java.util.*

class NormalFactory(
  private val executorComponent: ExecutorComponent,
): UnitFactory {
  
  override fun <T: Any> init(unitIdentify: UnitIdentify<T>, initStack: InitStack): T {
    @Suppress("UNCHECKED_CAST")
    return executorComponent.callConstructor(getConstructor(unitIdentify.unitClass), initStack) as T
  }
  
  companion object {
    //    @JvmStatic
//    fun initByContainer(unitClass: Class<*>, unitContainer: UnitContainer): Any {
//      val dependencies = unitClass.getAnnotation(Dependencies::class.java)
//      dependencies?.value?.forEach {
//        unitContainer.getUnitList(it.value.java, it.name)
//      }
//      if (unitClass.isAnnotationPresent(Metadata::class.java)) {
//        return initKotlinClass(unitClass, unitContainer)
//      }
//      return initNormalClass(unitClass, unitContainer)
//    }
//
//    @JvmStatic
//    fun initByContainer(unitClass: Class<*>, dependencies: List<Any>): Any {
//      if (unitClass.isAnnotationPresent(Metadata::class.java)) {
//        return initKotlinClass(unitClass, dependencies)
//      }
//      return initNormalClass(unitClass, dependencies)
//    }
//
    @JvmStatic
    fun getConstructor(unitClass: Class<*>): Constructor<*> {
      val constructors = unitClass.constructors
      if (constructors.size == 1) return constructors[0]
      for (constructor in constructors) {
        if (constructor.isAnnotationPresent(Inject::class.java)) {
          return constructor
        }
      }
      throw UnitException(unitClass, "could not get constructor")
    }

//    @JvmStatic
//    @Throws(UnitException::class)
//    private fun initKotlinClass(unitClass: Class<*>, dependencies: List<Any>): Any {
//      return try {
//        val field = unitClass.getField("INSTANCE")
//        field[null]
//      } catch (e: NoSuchFieldException) {
//        initNormalClass(unitClass, dependencies)
//      } catch (e: IllegalAccessException) {
//        throw UnitException(
//          "could not access INSTANCE field: $unitClass",
//          e
//        )
//      }
//    }
////
//    @JvmStatic
//    @Throws(UnitException::class)
//    private fun initNormalClass(unitClass: Class<*>, dependencies: List<Any>): Any {
//      val constructor = getConstructor(unitClass)
//      val parameters = constructor.parameters
//      return try {
//        constructor.newInstance(*dependencies.toTypedArray())
//      } catch (e: Throwable) {
//        throw UnitException(
//          "could not init " +
//          "unit: $unitClass " +
//          "\n parameter: ${parameters.map {it.type}} " +
//          "\n objects:   ${dependencies.map {it.javaClass}}",
//          e
//        )
//      }
//    }
  }
}