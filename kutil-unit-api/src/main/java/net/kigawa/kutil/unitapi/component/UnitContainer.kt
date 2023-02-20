@file:Suppress("unused")

package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.exception.*
import net.kigawa.kutil.unitapi.options.FindOptions
import java.util.*
import java.util.concurrent.*

interface UnitContainer: AutoCloseable {
  companion object {
    @JvmStatic
    var implementsClass: Class<out UnitContainer>? = null
    
    init {
      try {
        @Suppress("UNCHECKED_CAST")
        implementsClass = Class.forName("net.kigawa.kutil.unit.component.UnitContainerImpl") as Class<out UnitContainer>
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
  
  fun removeUnit(unitClass: Class<out Any>) {
    removeUnit(unitClass, null)
  }
  
  fun removeUnit(unitClass: Class<out Any>, name: String?) {
    removeUnit(UnitIdentify(unitClass, name))
  }
  
  fun removeUnit(identify: UnitIdentify<out Any>)
  
  fun <T: Any> getUnitList(identify: UnitIdentify<T>, findOptions: FindOptions): List<T>
  fun <T: Any> getUnitList(identify: UnitIdentify<T>): List<T> {
    return getUnitList(identify, FindOptions())
  }
  
  fun <T: Any> getUnitList(unitClass: Class<T>): List<T> {
    return getUnitList(unitClass, null)
  }
  
  fun <T: Any> getUnitList(unitClass: Class<T>, name: String?): List<T> {
    return getUnitList(UnitIdentify(unitClass, name))
  }
  
  fun <T: Any> getCorrespondingUnitList(identifies: List<UnitIdentify<out T>>, findOptions: FindOptions): List<T> {
    return identifies.map {
      getUnit(it, findOptions)
    }
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>): T {
    return getUnit(unitClass, null)
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>, findOptions: FindOptions): T {
    return getUnit(unitClass, null, findOptions)
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>, name: String?): T {
    return getUnit(UnitIdentify(unitClass, name))
  }
  
  fun <T: Any> getUnit(unitClass: Class<T>, name: String?, findOptions: FindOptions): T {
    return getUnit(UnitIdentify(unitClass, name), findOptions)
  }
  
  fun <T: Any> getUnit(identify: UnitIdentify<T>): T {
    return getUnit(identify, FindOptions())
  }
  
  fun <T: Any> getUnit(identify: UnitIdentify<T>, findOptions: FindOptions): T
  
  fun <T: Any> contain(identify: UnitIdentify<T>): Boolean {
    return getUnitList(identify).isNotEmpty()
  }
}