package net.kigawa.kutil.unit.classlist

import net.kigawa.kutil.unit.UnitException
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import java.io.File
import java.net.URL

class FileClassList(resource: URL, packageName: Package) : ClassList {
    override val classes: MutableList<Class<*>> = mutableListOf()
    override val errors: MutableList<Throwable> = mutableListOf()

    companion object {
        const val PROTOCOL = "file"
    }

    init {
        if (PROTOCOL != resource.protocol) throw RuntimeUnitException("could not support file type")
        loadUnit(File(resource.file), packageName.name)
    }

    private fun loadUnit(dir: File, packageName: String) {
        val files = dir.listFiles()
        if (files == null) {
            errors.add(UnitException("cold not load unit files"))
            return
        }
        for (file in files) {
            if (file.isDirectory) {
                loadUnit(file, packageName + "." + file.name)
                continue
            }
            if (!file.name.endsWith(".class")) continue
            var name = file.name
            name = name.replace(".class$".toRegex(), "")
            name = "$packageName.$name"
            try {
                classes.add(Class.forName(name))
            } catch (e: Exception) {
                errors.add(UnitException("cold not load unit: $name", e))
            }
        }
    }

}