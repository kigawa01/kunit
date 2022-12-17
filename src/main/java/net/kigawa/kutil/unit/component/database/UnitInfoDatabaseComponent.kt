package net.kigawa.kutil.unit.component.database

import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

interface UnitInfoDatabaseComponent {
  fun registerInfo(unitInfo: UnitInfo<*>)
  fun <T> findOneEquals(identify: UnitIdentify<T>): UnitInfo<T>?
  fun <T> findByClass(unitClass: Class<T>): List<UnitInfo<T>>
  fun <T> findOneByClass(unitClass: Class<T>): UnitInfo<T>? {
    val list = findByClass(unitClass)
    if (list.isEmpty()) return null
    if (list.size == 1) return list[0]
    throw NoSingleUnitException(unitClass, "unit is not single")
  }
  
  fun <T> findOneByEqualsOrClass(identify: UnitIdentify<T>): UnitInfo<T>? {
    return findOneEquals(identify) ?: findOneByClass(identify.unitClass)
  }
}