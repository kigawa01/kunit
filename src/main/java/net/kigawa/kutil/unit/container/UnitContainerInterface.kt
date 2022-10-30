package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.UnitException
import java.io.File

interface UnitContainerInterface
{
    @Deprecated("use loadUnits() and initUnits()")
    @Throws(UnitException::class)
    fun loadUnits(rootClass: Class<*>)
    {
        registerUnit(rootClass)
        initUnits()
    }

    fun registerJar(jarFile: File)
    fun registerUnits(rootClass: Class<*>): MutableList<Throwable>
    fun registerUnit(unit: Class<*>)
    fun getAllClass(): MutableList<Class<*>>
    fun <T> getUnit(unit: Class<T>): T?
    fun initUnits()
}