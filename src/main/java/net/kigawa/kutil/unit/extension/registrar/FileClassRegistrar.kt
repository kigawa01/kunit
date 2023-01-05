package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.util.AnnotationUtil
import java.io.File
import java.net.URL

@LateInit
class FileClassRegistrar(
  private val listRegistrar: ListRegistrar,
  private val loggerComponent: ContainerLoggerComponent,
): UnitRegistrar {
  companion object {
    const val PROTOCOL = "file"
  }
  
  fun register(resource: URL, packageName: String) {
    if (PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    listRegistrar.register(loadUnit(File(resource.file), packageName))
  }
  
  private fun loadUnit(dir: File, packageName: String): List<UnitIdentify<out Any>> {
    val identifies = mutableListOf<UnitIdentify<out Any>>()
    val files = dir.listFiles() ?: throw UnitException("cold not load unit files")
    for (file in files) {
      if (file.isDirectory) {
        identifies.addAll(loadUnit(file, packageName + "." + file.name))
        continue
      }
      if (!file.name.endsWith(".class")) continue
      var name = file.name
      name = name.replace(".class$".toRegex(), "")
      name = "$packageName.$name"
      loggerComponent.catch(null) {
        val unitClass = Class.forName(name)
        if (AnnotationUtil.hasUnitAnnotation(unitClass))
          identifies.add(UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)))
      }
    }
    return identifies
  }
}