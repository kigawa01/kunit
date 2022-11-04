package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.UnitException
import net.kigawa.kutil.unit.classlist.ClassList
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

    fun addLoader(loader: ClassList)
    fun addUnit(unit: Any)
    fun registerUnits(classList: ClassList): MutableList<Throwable>
    fun registerUnit(unitClass: Class<*>)
    fun registerJar(jarFile: File)
    fun getAllClass(): MutableList<Class<*>>
    fun <T> getUnit(unit: Class<T>): T?
    fun initUnits()
}