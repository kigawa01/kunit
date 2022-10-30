package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.Inject
import net.kigawa.kutil.unit.Unit
import net.kigawa.kutil.unit.UnitException
import java.lang.reflect.Constructor

class UnitInfo(private val unitClass: Class<*>, annotation: Unit? = null)
{
    var dependencies = listOf<Class<*>>()
        private set
    var unit: Any? = null
    var status: UnitStatus = UnitStatus.LOADED

    init
    {
        val list = mutableListOf<Class<*>>()
        annotation?.depended?.forEach {
            list.add(it.java)
        }
        try
        {
            constructor.parameterTypes.forEach {
                list.add(it)
            }
        } catch (_: UnitException)
        {
        }
        dependencies = list
    }

    @get:Throws(UnitException::class)
    val constructor: Constructor<*>
        get()
        {
            val constructors = unitClass.constructors
            if (constructors.size == 1) return constructors[0]
            for (constructor in constructors)
            {
                if (constructor.isAnnotationPresent(Inject::class.java))
                {
                    return constructor
                }
            }
            throw UnitException("could not get constructor: $unitClass")
        }
}