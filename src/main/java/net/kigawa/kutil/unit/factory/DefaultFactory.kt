package net.kigawa.kutil.unit.factory

import net.kigawa.kutil.unit.annotation.Dependencies
import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.exception.RuntimeUnitException
import net.kigawa.kutil.unit.exception.UnitException
import java.lang.reflect.Constructor
import java.util.*

class DefaultFactory : UnitFactory {
    override fun isValid(unitClass: Class<*>): Boolean {
        return unitClass.isAnnotationPresent(Unit::class.java)
    }

    override fun init(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        val unitAnnotation = unitClass.getAnnotation(Unit::class.java)
            ?: throw RuntimeUnitException("$unitClass is not supported. need @Unit")

        val dependencies = unitClass.getAnnotation(Dependencies::class.java)
        dependencies?.value?.forEach {
            unitContainer.getUnitList(it.value.java, it.name)
        }
        if (unitClass.isAnnotationPresent(Metadata::class.java)) {
            return initKotlinClass(unitClass, unitContainer)
        }
        return initNormalClass(unitClass, unitContainer)
    }

    fun getConstructor(unitClass: Class<*>): Constructor<*> {
        val constructors = unitClass.constructors
        if (constructors.size == 1) return constructors[0]
        for (constructor in constructors) {
            if (constructor.isAnnotationPresent(Inject::class.java)) {
                return constructor
            }
        }
        throw RuntimeUnitException("could not get constructor: $unitClass")
    }

    @Throws(UnitException::class)
    private fun initKotlinClass(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        return try {
            val field = unitClass.getField("INSTANCE")
            field[null]
        } catch (e: NoSuchFieldException) {
            initNormalClass(unitClass, unitContainer)
        } catch (e: IllegalAccessException) {
            throw UnitException(
                "could not access INSTANCE field: $unitClass",
                e
            )
        }
    }

    @Throws(UnitException::class)
    private fun initNormalClass(unitClass: Class<*>, unitContainer: UnitContainer): Any {
        val constructor = getConstructor(unitClass)
        val parameters = constructor.parameterTypes
        val objects = parameters.map {
            unitContainer.getUnit(it)
        }.toTypedArray()
        return try {
            constructor.newInstance(*objects)
        } catch (e: Throwable) {
            throw UnitException(
                "could not init " +
                        "unit: $unitClass " +
                        "\n parameter: ${parameters.map { it }} " +
                        "\n objects: ${objects.map { it.javaClass }}",
                e
            )
        }
    }
}