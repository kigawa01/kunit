package net.kigawa.kutil.unit.container

import java.util.*

class UnitIdentify(
    val unitClass: Class<*>,
    val name: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (other !is UnitIdentify) return false
        if (javaClass != other.javaClass) return false

        return unitClass == other.unitClass
                && name == other.name
    }

    override fun hashCode(): Int {
        return Objects.hash(unitClass, name)
    }
}