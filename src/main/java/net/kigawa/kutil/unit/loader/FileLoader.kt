package net.kigawa.kutil.unit.loader

import net.kigawa.kutil.unit.UnitException
import java.io.File
import java.util.function.Consumer

class FileLoader : UnitLoader
{

    override fun searchClass(rootClass: Class<*>, loader: Consumer<Class<*>>): List<Throwable>
    {
        val errors = mutableListOf<Throwable>()
        val rootPackage = rootClass.getPackage()
        val classLoader = rootClass.classLoader
        val resource = classLoader.getResource(rootPackage.name.replace('.', '/')) ?: return errors
        if ("file" != resource.protocol) return errors

        errors.addAll(loadUnit(File(resource.file), rootPackage.name, loader))

        return errors
    }

    private fun loadUnit(dir: File, packageName: String, loader: Consumer<Class<*>>): MutableList<Throwable>
    {
        val exceptions = mutableListOf<Throwable>()
        for (file in dir.listFiles() ?: throw UnitException("cold not load unit files"))
        {
            if (file.isDirectory)
            {
                loadUnit(file, packageName + "." + file.name, loader)
                continue
            }
            if (!file.name.endsWith(".class")) continue
            var name = file.name
            name = name.replace(".class$".toRegex(), "")
            name = "$packageName.$name"
            try
            {
                loader.accept(Class.forName(name))
            } catch (e: Exception)
            {
                exceptions.add(UnitException("cold not load unit: $name", e))
            }
        }
        return exceptions
    }
}