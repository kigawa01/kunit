package net.kigawa.kutil.unit.loader

import net.kigawa.kutil.unit.UnitException
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.util.*
import java.util.function.Consumer

class JarfileLoader : UnitLoader
{

    override fun searchClass(rootClass: Class<*>, loader: Consumer<Class<*>>): List<Throwable>
    {
        val errors = mutableListOf<Throwable>()
        val packageName = rootClass.getPackage()
        val classLoader = rootClass.classLoader
        val resource = classLoader.getResource(packageName.name.replace('.', '/')) ?: return errors
        if ("jar" != resource.protocol) return errors

        try
        {
            (resource.openConnection() as JarURLConnection).jarFile.use { jarFile ->
                for (entry in Collections.list(jarFile.entries()))
                {
                    var name = entry.name
                    if (!name.startsWith(packageName.name.replace('.', '/'))) continue
                    if (!name.endsWith(".class")) continue
                    name = name.replace('/', '.').replace(".class$".toRegex(), "")
                    try
                    {
                        loader.accept(Class.forName(name))
                    } catch (e: Exception)
                    {
                        errors.add(UnitException("could not load unit: $name", e))
                    }
                }
            }
        } catch (e: IOException)
        {
            errors.add(RuntimeUnitException("could not load units file", e))
        }


        return errors
    }
}