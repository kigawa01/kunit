package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.runtimeexception.NoFoundUnitException
import net.kigawa.kutil.unit.runtimeexception.NoSingleUnitException
import net.kigawa.kutil.unit.util.Util

class UnitsList {
    private val infoList = mutableListOf<UnitInfo>()
    private val interfaceMap: MutableMap<Class<*>, Class<*>> = HashMap()

    fun put(unitInfo: UnitInfo) {
        synchronized(infoList) {
            infoList.add(unitInfo)
        }
    }

    fun getUnits(unitClass: Class<*>, name: String?): List<UnitInfo> {
        return synchronized(infoList) {
            infoList.filter {
                if (!Util.instanceOf(it.unitClass, unitClass)) return@filter false
                if (name == null) return@filter true
                it.name == name
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
            infoList.map { UnitIdentify(it.unitClass, it.name) }
        }
    }
}