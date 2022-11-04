package net.kigawa.kutil.unit.container

import net.kigawa.kutil.unit.Inject
import net.kigawa.kutil.unit.Unit
import net.kigawa.kutil.unit.UnitException
import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.factory.UnitFactory
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import net.kigawa.kutil.unit.runtimeexception.UnitNotInitException
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.util.*

class UnitContainer(
    private val parent: UnitContainer? = null,
    vararg units: Any,
) : UnitContainerInterface
{
    constructor(vararg units: Any) : this(null, *units)

    private val unitInfoMap = UnitsMap()
    private val loaders = mutableSetOf<ClassList>()
    private val factories = mutableSetOf<UnitFactory>()

    init
    {
        addUnit(this)
        for (unit in units)
        {
            addUnit(unit)
        }
    }

    override fun addLoader(loader: ClassList)
    {
        loaders.add(loader)
    }
    override fun registerUnit(unitClass: Class<*>)
    {
        unitInfoMap.put(unitClass, UnitInfo(unitClass))
    }

    override fun registerUnits(classList: ClassList): MutableList<Throwable>
    {
        val errors = mutableListOf<Throwable>()

        classList.classes.forEach {
            try
            {
                registerUnit(it)
            } catch (e: Throwable)
            {
                errors.add(e)
            }
        }

        return errors
    }

    override fun getAllClass(): MutableList<Class<*>>
    {
        val list = mutableListOf<Class<*>>()
        list.addAll(unitInfoMap.keySet())
        parent?.let { list.addAll(it.getAllClass()) }
        return list
    }

    override fun initUnits()
    {
        val exceptions = LinkedList<Exception>()
        for (unitClass in unitInfoMap.keySet())
        {
            try
            {
                initUnit(unitClass)
            } catch (e: Throwable)
            {
                exceptions.add(UnitException("could not init unit: $unitClass", e))
            }
        }
        throwExceptions(exceptions, RuntimeUnitException("there are exceptions when init units"))
    }

    override fun registerJar(jarFile: File)
    {
        TODO("Not yet implemented")
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getUnit(unitClass: Class<T>): T?
    {
        val unitInfo = unitInfoMap.get(unitClass)
        if (unitInfo != null)
        {
            if (unitInfo.unit != null) return unitInfo.unit as T
            throw UnitNotInitException("unit is not initialized")
        }
        if (parent != null) return parent.getUnit(unitClass)
        throw RuntimeUnitException("unit is not found: $unitClass")
    }

    override fun addUnit(unit: Any)
    {
        registerUnit(unit.javaClass)
        (unitInfoMap.get(unit.javaClass) ?: throw RuntimeUnitException("could not register unit"))
            .unit = unit
    }


    private fun <T> getUnitAndInit(unitClass: Class<T>): T?
    {
        val unitInfo = unitInfoMap.get(unitClass)
        if (unitInfo != null)
        {
            return try
            {
                getUnit(unitClass)
            } catch (e: UnitNotInitException)
            {
                initUnit(unitClass)
            }
        }
        if (parent != null) return parent.getUnitAndInit(unitClass)
        throw RuntimeUnitException("unit is not found: $unitClass")
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(UnitException::class)
    private fun <T> initUnit(unitClass: Class<T>): T
    {
        val unitInfo = unitInfoMap.get(unitClass) ?: throw UnitException("could not find unit: $unitClass")
        if (unitInfo.unit != null) return unitInfo.unit as T
        if (unitClass.isAnnotationPresent(Metadata::class.java))
        {
            unitInfo.unit = initKotlinClass(unitClass, unitInfo)
            return unitInfo.unit as T
        }
        unitInfo.unit = initNormalClass(unitClass, unitInfo)
        return unitInfo.unit as T
    }

    @Throws(UnitException::class)
    private fun initKotlinClass(unitClass: Class<*>, unitInfo: UnitInfo): Any
    {
        return try
        {
            val field = unitClass.getField("INSTANCE")
            field[null]
        } catch (e: NoSuchFieldException)
        {
            initNormalClass(unitClass, unitInfo)
        } catch (e: IllegalAccessException)
        {
            throw UnitException("could not access INSTANCE field: ", e)
        }
    }

    @Throws(UnitException::class)
    private fun initNormalClass(unitClass: Class<*>, unitInfo: UnitInfo): Any
    {
        val constructor = unitInfo.getConstructor(Inject::class.java)
        val parameters = constructor.parameterTypes
        val objects = arrayOfNulls<Any>(parameters.size)
        for (i in parameters.indices)
        {
            objects[i] = getUnitAndInit(parameters[i])
        }
        return try
        {
            constructor.newInstance(*objects)
        } catch (e: InstantiationException)
        {
            throw UnitException("could not init unit: $unitClass", e)
        } catch (e: IllegalAccessException)
        {
            throw UnitException("could not init unit: $unitClass", e)
        } catch (e: InvocationTargetException)
        {
            throw UnitException("could not init unit: $unitClass", e.cause)
        }
    }

    @Throws(Throwable::class)
    private fun <E : Throwable> throwExceptions(exceptions: List<Exception>, base: E)
    {
        if (exceptions.isEmpty()) return
        exceptions.forEach {
            base.addSuppressed(it)
        }
        throw base
    }
}