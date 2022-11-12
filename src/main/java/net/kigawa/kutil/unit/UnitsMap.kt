package net.kigawa.kutil.unit

class UnitsMap {
    private val infoMap: MutableMap<Class<*>, UnitInfo> = HashMap()
    private val interfaceMap: MutableMap<Class<*>, Class<*>> = HashMap()

    fun put(clazz: Class<*>, unitInfo: UnitInfo) {
        infoMap[clazz] = unitInfo
    }

    fun get(clazz: Class<*>): UnitInfo?{
        val result = infoMap[clazz]
        if (result != null) return result

        val unitClass = interfaceMap[clazz]
        if (unitClass != null) return infoMap[unitClass]

        var foundClass: Class<*>? = null
        for (entry in infoMap.entries) {
            if (!entry.key.interfaces.contains(clazz)) continue

            if (foundClass != null)
                throw RuntimeUnitException("interface must implemented by only one unit: " + foundClass + entry.key)
            foundClass = entry.key
        }

        if (foundClass == null) return null
        interfaceMap[clazz] = foundClass
        return infoMap[foundClass]
    }

    fun keySet(): Set<Class<*>> {
        return infoMap.keys
    }

    fun clearCache() {
        interfaceMap.clear()
    }
}