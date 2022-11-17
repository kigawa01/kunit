package net.kigawa.kutil.unit.concurrent

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.UnitInfo
import net.kigawa.kutil.unit.exception.NoFoundUnitException
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import net.kigawa.kutil.unit.util.Util

class UnitsList {
    private val infoList = mutableSetOf<UnitInfo>()
    
    fun contain(unitClass: Class<*>, name: String?): Boolean {
        return synchronized(infoList) {
            return@synchronized infoList.any {isSameIdentify(it.unitClass, unitClass, it.name, name)}
        }
    }
    
    fun put(unitInfo: UnitInfo) {
        synchronized(infoList) {
            infoList.removeIf {isSameIdentify(it.unitClass, unitInfo.unitClass, it.name, unitInfo.name)}
            infoList.add(unitInfo)
        }
    }
    
    fun getUnits(unitClass: Class<*>, name: String?): List<UnitInfo> {
        return synchronized(infoList) {
            infoList.filter {
                if (!Util.instanceOf(it.unitClass, unitClass)) return@filter false
                isSameIdentify(null, null, name, it.name)
            }
        }
    }
    
    fun getUnit(unitClass: Class<*>, name: String?): UnitInfo {
        val list = getUnits(unitClass, name)
        if (list.isEmpty()) throw NoFoundUnitException("unit is not found class: $unitClass name: $name")
        if (list.size != 1) throw NoSingleUnitException("not only single unit found class: $unitClass name: $name")
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