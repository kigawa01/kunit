package net.kigawa.kutil.unit.factory

import net.kigawa.kutil.unit.annotation.Dependencies
import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.exception.RuntimeUnitException
import net.kigawa.kutil.unit.exception.UnitException
import java.lang.reflect.Constructor
import java.util.*
import kotlin.Any
import kotlin.Boolean
import kotlin.Metadata
import kotlin.Throwable
import kotlin.Throws

class DefaultFactory: UnitFactory {
  override fun isValid(unitClass: Class<*>): Boolean {
    return unitClass.isAnnotationPresent(Unit::class.java)
  }
  
  override fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any {
    return initObject(unitClass, unitContainer)
  }
  
  companion object {
    @JvmStatic
    fun initObject(unitClass: Class<*>, unitContainer: UnitContainer): Any {
      val dependencies = unitClass.getAnnotation(Dependencies::class.java)
      dependencies?.value?.forEach {
        unitContainer.getUnitList(it.value.java, it.name)
      }
      if (unitClass.isAnnotationPresent(Metadata::class.java)) {
        return initKotlinClass(unitClass, unitContainer)
      }
      return initNormalClass(unitClass, unitContainer)
    }
    
    @JvmStatic
    fun getConstructor(unitClass: Class<*>): Constructor<*> {
      val constructors = unitClass.constructors
      if (constructors.size == 1) return constructors[0]
      for (constructor in constructors) {
        if (constructor.isAnnotationPresent(Inject::class.java)) {
          return constructor
        }
      }
      throw RuntimeUnitException(unitClass, "could not get constructor")
    }
    
    @JvmStatic
    @Throws(UnitException::class)
    private fun initKotlinClass(unitClass: Class<*>, unitContainer: UnitContainer): Any {
      return try {
        val field = unitClass.getField("INSTANCE")
        field[null]
      } catch (e: NoSuchFieldException) {
        initNormalClass(unitClass, unitContainer)
      } catch (e: IllegalAccessException) {
        throw UnitException(
          "could not access INSTANCE field: $unitClass",
          e
        )
      }
    }
    
    @JvmStatic
    @Throws(UnitException::class)
    private fun initNormalClass(unitClass: Class<*>, unitContainer: UnitContainer): Any {
      val constructor = getConstructor(unitClass)
      val parameters = constructor.parameters
      val objects = parameters.map {
        if (unitContainer.contain(it.type, it.name)) unitContainer.getUnit(it.type, it.name)
        else unitContainer.getUnit(it.type)
      }.toTypedArray()
      return try {
        constructor.newInstance(*objects)
      } catch (e: Throwable) {
        throw UnitException(
          "could not init " +
          "unit: $unitClass " +
          "\n parameter: ${parameters.map {it}} " +
          "\n objects: ${objects.map {it.javaClass}}",
          e
        )
      }
    }
  }
}