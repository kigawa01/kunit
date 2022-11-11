package net.kigawa.kutil.unit.factory

import net.kigawa.kutil.unit.container.UnitContainerInterface
import java.util.concurrent.Future

interface UnitFactory {
    fun isValid(unitClass: Class<*>): Boolean

    fun init(unitClass: Class<*>, unitContainer: UnitContainerInterface): Any
}