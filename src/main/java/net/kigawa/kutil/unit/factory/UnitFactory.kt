package net.kigawa.kutil.unit.factory

import net.kigawa.kutil.unit.container.UnitContainer

interface UnitFactory {
    fun isValid(unitClass: Class<*>): Boolean

    fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any
}