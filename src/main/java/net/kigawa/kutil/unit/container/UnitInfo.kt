package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import java.lang.reflect.Constructor
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

class UnitInfo(val unitClass: Class<*>, val name: String?) {
    var status: UnitStatus = UnitStatus.NOT_LOADED
        private set

    var unit: Any? = null
        set(value) {
            field = value
            status = UnitStatus.INITIALIZED
        }
    var future: FutureTask<*>? = null
        set(value) {
            field = value
            status = UnitStatus.INITIALIZING
        }
    var factory: UnitFactory? = null
        set(value) {
            field = value
            status = if (field == null) UnitStatus.NOT_LOADED
            else UnitStatus.LOADED
        }

    constructor(unitClass: Class<*>) : this(unitClass, null)

    fun getConstructor(annotationClass: Class<out Annotation>): Constructor<*> {
        val constructors = unitClass.constructors
        if (constructors.size == 1) return constructors[0]
        for (constructor in constructors) {
            if (constructor.isAnnotationPresent(annotationClass)) {
                return constructor
            }
        }
        throw RuntimeUnitException("could not get constructor: $unitClass")
    }
}