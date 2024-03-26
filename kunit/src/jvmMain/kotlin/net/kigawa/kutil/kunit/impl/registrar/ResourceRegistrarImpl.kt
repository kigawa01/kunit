package net.kigawa.kutil.kunit.impl.registrar

import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.UnitLoggerComponent
import net.kigawa.kutil.kunit.api.exception.UnitException
import net.kigawa.kutil.kunit.api.extention.Message
import net.kigawa.kutil.kunit.api.registrar.ResourceRegistrar
import net.kigawa.kutil.kunit.api.registrar.SelectionRegistrar
import net.kigawa.kutil.kunit.impl.util.LocaleBuilder
import java.io.File
import java.net.JarURLConnection
import java.net.URL
import java.util.*
import java.util.logging.Level

@LateInit
class ResourceRegistrarImpl(
  private val loggerComponent: UnitLoggerComponent,
  private val selectionRegistrar: SelectionRegistrar,
) : ResourceRegistrar {
  companion object {
    const val JAR_PROTOCOL = "jar"
    const val FILE_PROTOCOL = "file"
  }

  override fun register(classLoader: ClassLoader, packageName: String, selectionRegistrar: SelectionRegistrar) {
    val packageDir = packageName.replace('.', '/')
    classLoader.getResources(packageDir).asIterator().forEach {
      when (it.protocol) {
        JAR_PROTOCOL -> registerJar(classLoader, it, packageName, selectionRegistrar)
        FILE_PROTOCOL -> registerFile(classLoader, it, packageName, selectionRegistrar)
        else -> throw UnitException("could not support resource protocol")
      }
    }
  }

  override fun register(classLoader: ClassLoader, packageName: String) {
    register(classLoader, packageName, selectionRegistrar)
  }

  private fun tryRegister(
    classLoader: ClassLoader,
    className: String,
    selectionRegistrar: SelectionRegistrar,
    errInfo: List<Any>,
  ): (() -> Unit)? {
    return try {
      val unitClass = classLoader.loadClass(className)
      selectionRegistrar.selectRegister(unitClass)
    } catch (e: Throwable) {
      loggerComponent.log(
        Message(
          Level.WARNING,
          LocaleBuilder("could not register class").toString(),
          listOf(e),
          errInfo
        )
      )
      null
    }
  }

  private fun registerJar(
    classLoader: ClassLoader,
    resource: URL,
    packageName: String,
    selectionRegistrar: SelectionRegistrar,
  ) {
    if (JAR_PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")

    (resource.openConnection() as JarURLConnection).jarFile.use { jarFile ->
      Collections.list(jarFile.entries()).map {
        var name = it.name
        if (!name.startsWith(packageName.replace('.', '/'))) return@map null
        if (!name.endsWith(".class")) return@map null
        name = name.replace('/', '.').replace(".class$".toRegex(), "")
        return@map tryRegister(classLoader, name, selectionRegistrar, listOf(name, classLoader, resource))
      }
    }.forEach { it?.invoke() }
  }

  private fun registerFile(
    classLoader: ClassLoader,
    resource: URL,
    packageName: String,
    selectionRegistrar: SelectionRegistrar,
  ) {
    if (FILE_PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    loadUnit(classLoader, File(resource.file), packageName, selectionRegistrar).forEach { it?.invoke() }
  }

  private fun loadUnit(
    classLoader: ClassLoader,
    dir: File,
    packageName: String,
    selectionRegistrar: SelectionRegistrar,
  ): List<(() -> Unit)?> {
    val files = dir.listFiles() ?: throw UnitException("cold not load unit files")
    return files.map { file ->
      if (file.isDirectory) {
        val list = loadUnit(classLoader, file, packageName + "." + file.name, selectionRegistrar)
        return@map { list.forEach { it?.invoke() } }
      }
      if (!file.name.endsWith(".class")) return@map null
      var name = file.name
      name = name.replace(".class$".toRegex(), "")
      name = "$packageName.$name"
      tryRegister(classLoader, name, selectionRegistrar, listOf(name, classLoader, dir))
    }
  }
}