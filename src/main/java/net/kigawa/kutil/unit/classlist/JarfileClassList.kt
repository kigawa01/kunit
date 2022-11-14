package net.kigawa.kutil.unit.classlist

import net.kigawa.kutil.unit.exception.RuntimeUnitException
import net.kigawa.kutil.unit.exception.UnitException
import java.io.File
import java.io.IOException
import java.net.JarURLConnection
import java.net.URL
import java.util.*

class JarfileClassList(resource: URL, packageName: String) : ClassList {

    override val classes: MutableList<Class<*>> = mutableListOf()
    override val errors: MutableList<Throwable> = mutableListOf()

    companion object {
        const val PROTOCOL = "jar"
    }

    constructor(resource: URL, packageName: Package) : this(resource, packageName.name)
    constructor(jarFile: File) : this(jarFile.toURI().toURL(), "")

    init {
        if (PROTOCOL != resource.protocol) throw RuntimeUnitException("could not support file type")

        try {
            (resource.openConnection() as JarURLConnection).jarFile.use { jarFile ->
                for (entry in Collections.list(jarFile.entries())) {
                    var name = entry.name
                    if (!name.startsWith(packageName.replace('.', '/'))) continue
                    if (!name.endsWith(".class")) continue
                    name = name.replace('/', '.').replace(".class$".toRegex(), "")
                    try {
                        classes.add(Class.forName(name))
                    } catch (e: Throwable) {
                        errors.add(UnitException("could not load unit: $name", e))
                    }
                }
            }
        } catch (e: IOException) {
            errors.add(RuntimeUnitException("could not load units file", e))
        }
    }

}