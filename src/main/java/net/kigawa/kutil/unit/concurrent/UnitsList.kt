package net.kigawa.kutil.unit.concurrent

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.UnitInfo
import net.kigawa.kutil.unit.exception.NoFoundUnitException
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import net.kigawa.kutil.unit.util.Util

class UnitsList {
  private val infoList = mutableSetOf<UnitInfo>()
  
  fun contain(unitInfo: UnitInfo): Boolean {
    return synchronized(infoList) {
      return@synchronized infoList.any {it.unitClass == unitInfo.unitClass && it.name == unitInfo.name}
    }
  }
  
  fun put(unitInfo: UnitInfo) {
    synchronized(infoList) {
      infoList.removeIf {it.unitClass == unitInfo.unitClass && it.name == unitInfo.name}
      infoList.add(unitInfo)
    }
  }
  
  fun getUnits(unitClass: Class<*>, name: String?): List<UnitInfo> {
    return synchronized(infoList) {
      infoList.filter {
        if (!Util.instanceOf(it.unitClass, unitClass)) return@filter false
        if (name == null) return@filter true
        if (name == "") return@filter true
        true
      }
    }
  }
  
  fun getUnit(unitClass: Class<*>, name: String?): UnitInfo {
    val list = getUnits(unitClass, name)
    if (list.isEmpty()) throw NoFoundUnitException(unitClass, name, "unit is not found")
    if (list.size != 1) throw NoSingleUnitException(unitClass, name, "not only single unit found")
    return list[0]
  }
  
  fun unitKeys(): List<UnitIdentify> {
    return synchronized(infoList) {
      infoList.map {UnitIdentify(it.unitClass, it.name)}
    }
  }
  
  fun isSameIdentify(unitClass: Class<*>?, unitClass1: Class<*>?, name: String?, name1: String?): Boolean {
    return unitClass == unitClass1 && (name ?: "") == (name1 ?: "")
  }
}