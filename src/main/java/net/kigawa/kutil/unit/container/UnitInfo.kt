package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.Inject
import net.kigawa.kutil.unit.Unit
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import java.lang.reflect.Constructor

class UnitInfo(private val unitClass: Class<*>)
{
    var dependencies = listOf<Class<*>>()
        private set
    var unit: Any? = null
    var status: UnitStatus = UnitStatus.LOADED

    init
    {
        val list = mutableListOf<Class<*>>()

        unitClass.getAnnotation(Unit::class.java)?.depended?.forEach {
            list.add(it.java)
        }
        getConstructor(Inject::class.java).parameterTypes.forEach {
            list.add(it)
        }

        dependencies = list
    }

    fun getConstructor(annotationClass: Class<out Annotation>): Constructor<*>
    {
        val constructors = unitClass.constructors
        if (constructors.size == 1) return constructors[0]
        for (constructor in constructors)
        {
            if (constructor.isAnnotationPresent(annotationClass))
            {
                return constructor
            }
        }
        throw RuntimeUnitException("could not get constructor: $unitClass")
    }
}