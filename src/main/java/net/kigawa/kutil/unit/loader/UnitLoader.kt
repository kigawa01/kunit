package net.kigawa.kutil.unit.loader

import net.kigawa.kutil.unit.container.UnitInfo
import java.util.function.Consumer

interface UnitLoader
{
    fun searchClass(rootClass: Class<*>, loader: Consumer<Class<*>>): List<Throwable>
}