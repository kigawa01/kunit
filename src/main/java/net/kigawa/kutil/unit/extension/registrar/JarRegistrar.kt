package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.*
import java.net.JarURLConnection
import java.net.URL
import java.util.*

@LateInit
class JarRegistrar(
  private val loggerComponent: UnitLoggerComponent,
  getterComponent: UnitStoreComponent, databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
): SelectionRegistrar(getterComponent, databaseComponent, container) {
  companion object {
    const val PROTOCOL = "jar"
  }
  
  fun register(resource: URL, packageName: String) {
    if (PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    
    (resource.openConnection() as JarURLConnection).jarFile.use {jarFile->
      Collections.list(jarFile.entries()).map {
        var name = it.name
        if (!name.startsWith(packageName.replace('.', '/'))) return@map null
        if (!name.endsWith(".class")) return@map null
        name = name.replace('/', '.').replace(".class$".toRegex(), "")
        loggerComponent.catch(null) {
          val unitClass = Class.forName(name)
          selectRegister(unitClass)
        }
      }
    }.forEach {it?.invoke()}
  }
}