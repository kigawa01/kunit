package net.kigawa.kutil.unit.extension.identify

import net.kigawa.kutil.unit.exception.UnitException
import java.io.File
import java.net.URL

class FileUnitIdentifies(resource: URL, packageName: Package): UnitIdentifies {
  override val classes: MutableList<UnitIdentify<*>> = mutableListOf()
  override val errors: MutableList<Throwable> = mutableListOf()
  
  companion object {
    const val PROTOCOL = "file"
  }
  
  init {
    if (PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
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
        classes.add(UnitIdentify(Class.forName(name), null))
      } catch (e: Throwable) {
        errors.add(
          UnitException(
            "cold not load unit: $name",
            e
          )
        )
      }
    }
  }
}