package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import net.kigawa.kutil.unit.runtimeexception.UnitNotInitException
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.net.JarURLConnection
import java.net.URL
import java.util.*

class UnitContainer(
    private val parent: UnitContainer? = null,
    vararg units: Any,
)
{
    constructor(vararg units: Any) : this(null, *units)

    private val unitInfoMap = UnitsMap()

    init
    {
        registerUnit(this)
        for (unit in units)
        {
            registerUnit(unit)
        }
    }

    @Throws(UnitException::class)
    fun loadUnits(rootClass: Class<*>)
    {
        registerUnits(rootClass)
        initUnits()
    }

    fun <T : Any> registerUnit(unit: T)
    {
        val containerInfo = UnitInfo(unit.javaClass)
        containerInfo.unit = unit
        unitInfoMap.put(unit.javaClass, containerInfo)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getUnit(unitClass: Class<T>): T?
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

    val allClasses: Set<Class<*>>
        get() = unitInfoMap.keySet()

    fun initUnits()
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
        val constructor = unitInfo.constructor
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

    fun registerUnits(rootClass: Class<*>)
    {
        unitInfoMap.clearCache()
        val rootPackage = rootClass.getPackage()
        val classLoader = rootClass.classLoader
        val root = classLoader.getResource(rootPackage.name.replace('.', '/'))
        val exceptions = LinkedList<Exception>()
        if (root == null) throw RuntimeUnitException("could not load class files")
        if ("file" == root.protocol)
        {
            registerUnit(File(root.file), rootPackage.name)
        } else if ("jar" == root.protocol)
        {
            registerUnit(root, rootPackage.name)
        }
        throwExceptions(exceptions, RuntimeUnitException("there are exceptions when load units"))
    }

    private fun registerUnit(root: URL, packageName: String): MutableList<Throwable>
    {
        val exceptions = mutableListOf<Throwable>()
        try
        {
            (root.openConnection() as JarURLConnection).jarFile.use { jarFile ->
                for (entry in Collections.list(jarFile.entries()))
                {
                    var name = entry.name
                    if (!name.startsWith(packageName.replace('.', '/'))) continue
                    if (!name.endsWith(".class")) continue
                    name = name.replace('/', '.').replace(".class$".toRegex(), "")
                    try
                    {
                        registerUnit(Class.forName(name))
                    } catch (e: Exception)
                    {
                        exceptions.add(UnitException("could not load unit: $name", e))
                    }
                }
            }
        } catch (e: IOException)
        {
            exceptions.add(RuntimeUnitException("could not load units file", e))
        }
        return exceptions
    }


    private fun registerUnit(dir: File, packageName: String): MutableList<Throwable>
    {
        val exceptions = mutableListOf<Throwable>()
        for (file in dir.listFiles() ?: throw UnitException("cold not load unit files"))
        {
            if (file.isDirectory)
            {
                registerUnit(file, packageName + "." + file.name)
                continue
            }
            if (!file.name.endsWith(".class")) continue
            var name = file.name
            name = name.replace(".class$".toRegex(), "")
            name = "$packageName.$name"
            try
            {
                registerUnit(Class.forName(name))
            } catch (e: Exception)
            {
                exceptions.add(UnitException("cold not load unit: $name", e))
            }
        }
        return exceptions
    }

    private fun registerUnit(unitClass: Class<*>)
    {
        val annotation = unitClass.getAnnotation(Unit::class.java) ?: return
        unitInfoMap.put(unitClass, UnitInfo(unitClass, annotation))
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