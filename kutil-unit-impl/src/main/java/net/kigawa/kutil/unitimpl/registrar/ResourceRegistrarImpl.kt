package net.kigawa.kutil.unitimpl.registrar

import net.kigawa.kutil.unit.util.LocaleBuilder
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.extention.Message
import net.kigawa.kutil.unitapi.registrar.ResourceRegistrar
import java.io.File
import java.net.JarURLConnection
import java.net.URL
import java.util.*
import java.util.logging.Level

@LateInit
class ResourceRegistrarImpl(
  private val loggerComponent: UnitLoggerComponent,
  getterComponent: UnitStoreComponent,
  databaseComponent: UnitDatabaseComponent,
  container: UnitContainer,
): SelectionRegistrarImpl(getterComponent, databaseComponent, container), ResourceRegistrar {
  companion object {
    const val JAR_PROTOCOL = "jar"
    const val FILE_PROTOCOL = "file"
  }
  
  override fun register(classLoader: ClassLoader, packageName: String) {
    val packageDir = packageName.replace('.', '/')
    classLoader.getResources(packageDir).asIterator().forEach {
      when (it.protocol) {
        JAR_PROTOCOL ->registerJar(classLoader, it, packageName)
        FILE_PROTOCOL->registerFile(classLoader, it, packageName)
        else         ->throw UnitException("could not support resource protocol")
      }
    }
  }
  
  private fun registerJar(classLoader: ClassLoader, resource: URL, packageName: String) {
    if (JAR_PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    
    (resource.openConnection() as JarURLConnection).jarFile.use {jarFile->
      Collections.list(jarFile.entries()).map {
        var name = it.name
        if (!name.startsWith(packageName.replace('.', '/'))) return@map null
        if (!name.endsWith(".class")) return@map null
        name = name.replace('/', '.').replace(".class$".toRegex(), "")
        try {
          val unitClass = classLoader.loadClass(name)
          selectRegister(unitClass)
        } catch (e: Throwable) {
          loggerComponent.log(
            Message(
              Level.WARNING,
              LocaleBuilder("could not register class").toString(),
              listOf(e),
              listOf(name, classLoader, resource)
            )
          )
          null
        }
      }
    }.forEach {it?.invoke()}
  }
  
  private fun registerFile(classLoader: ClassLoader, resource: URL, packageName: String) {
    if (FILE_PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    loadUnit(classLoader, File(resource.file), packageName).forEach {it?.invoke()}
  }
  
  private fun loadUnit(classLoader: ClassLoader, dir: File, packageName: String): List<(()->Unit)?> {
    val files = dir.listFiles() ?: throw UnitException("cold not load unit files")
    return files.map {file->
      if (file.isDirectory) {
        val list = loadUnit(classLoader, file, packageName + "." + file.name)
        return@map {list.forEach {it?.invoke()}}
      }
      if (!file.name.endsWith(".class")) return@map null
      var name = file.name
      name = name.replace(".class$".toRegex(), "")
      name = "$packageName.$name"
      try {
        val unitClass = classLoader.loadClass(name)
        selectRegister(unitClass)
      } catch (e: Throwable) {
        loggerComponent.log(
          Message(
            Level.WARNING,
            LocaleBuilder("could not register class").toString(),
            listOf(e),
            listOf(name, classLoader, dir)
          )
        )
        null
      }
    }
  }
}