package net.kigawa.kutil.unit.classlist

import net.kigawa.kutil.unit.UnitException
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.util.*

class JarfileClassList(resource: URL, packageName: Package) : ClassList {

    override val classes: MutableList<Class<*>> = mutableListOf()
    override val errors: MutableList<Throwable> = mutableListOf()

    companion object {
        const val PROTOCOL = "jar"
    }

    init {
        if (PROTOCOL != resource.protocol) throw RuntimeUnitException("could not support file type")

        try {
            (resource.openConnection() as JarURLConnection).jarFile.use { jarFile ->
                for (entry in Collections.list(jarFile.entries())) {
                    var name = entry.name
                    if (!name.startsWith(packageName.name.replace('.', '/'))) continue
                    if (!name.endsWith(".class")) continue
                    name = name.replace('/', '.').replace(".class$".toRegex(), "")
                    try {
                        classes.add(Class.forName(name))
                    } catch (e: Exception) {
                        errors.add(UnitException("could not load unit: $name", e))
                    }
                }
            }
        } catch (e: IOException) {
            errors.add(RuntimeUnitException("could not load units file", e))
        }
    }

}