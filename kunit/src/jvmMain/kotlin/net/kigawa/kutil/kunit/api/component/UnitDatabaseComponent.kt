@file:Suppress("unused")

package net.kigawa.kutil.kunit.api.component

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.exception.NoSingleUnitException
import net.kigawa.kutil.kunit.api.extention.*
import net.kigawa.kutil.kunit.api.options.RegisterOptions

interface UnitDatabaseComponent {
  fun getComponentDatabase(): ComponentDatabase
  
  fun addDatabase(unitInfoDatabase: UnitInfoDatabase)
  fun removeDatabase(unitInfoDatabase: UnitInfoDatabase)
  fun registerInfo(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions)
  fun unregisterInfo(unitInfo: UnitInfo<*>)
  fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>>
  fun <T: Any> findByClass(unitClass: Class<T>): List<UnitInfo<T>> {
    return findByIdentify(UnitIdentify(unitClass, null))
  }
  
  fun <T: Any> findOneByClass(unitClass: Class<T>): UnitInfo<T>? {
    return findOneByIdentify(UnitIdentify(unitClass, null))
  }
  
  fun <T: Any> findOneByIdentify(identify: UnitIdentify<T>): UnitInfo<T>? {
    val list = findByIdentify(identify)
    if (list.isEmpty()) return null
    if (list.size == 1) return list[0]
    throw NoSingleUnitException("unitInfo is not single", identify = identify, infoList = list)
  }
  
  fun <T: Any> findOneByEqualsOrClass(identify: UnitIdentify<T>): UnitInfo<T>? {
    return findOneByIdentify(identify) ?: findOneByClass(identify.unitClass)
  }
}