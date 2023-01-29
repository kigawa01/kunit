package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import net.kigawa.kutil.unit.exception.UnitException
import java.io.File
import java.net.URL

@LateInit
class FileClassRegistrar(
  private val loggerComponent: UnitLoggerComponent, getterComponent: UnitGetterComponent,
  databaseComponent: UnitDatabaseComponent, container: UnitContainer,
): SelectionRegistrar(getterComponent, databaseComponent, container) {
  companion object {
    const val PROTOCOL = "file"
  }
  
  fun register(resource: URL, packageName: String) {
    if (PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    loadUnit(File(resource.file), packageName).forEach {it?.invoke()}
  }
  
  private fun loadUnit(dir: File, packageName: String): List<(()->Unit)?> {
    val files = dir.listFiles() ?: throw UnitException("cold not load unit files")
    return files.map {file->
      if (file.isDirectory) {
        val list = loadUnit(file, packageName + "." + file.name)
        return@map {list.forEach {it?.invoke()}}
      }
      if (!file.name.endsWith(".class")) return@map null
      var name = file.name
      name = name.replace(".class$".toRegex(), "")
      name = "$packageName.$name"
      loggerComponent.catch(null) {
        val unitClass = Class.forName(name)
        selectRegister(unitClass)
      }
    }
  }
}