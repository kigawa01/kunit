package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.util.AnnotationUtil
import java.net.JarURLConnection
import java.net.URL
import java.util.*

@LateInit
class JarRegistrar(
  private val listRegistrar: ListRegistrar,
  private val loggerComponent: ContainerLoggerComponent,
): UnitRegistrar {
  companion object {
    const val PROTOCOL = "jar"
  }
  
  fun register(resource: URL, packageName: String) {
    if (PROTOCOL != resource.protocol) throw RuntimeException("could not support file type")
    
    val identifies = mutableListOf<UnitIdentify<out Any>>()
    (resource.openConnection() as JarURLConnection).jarFile.use {jarFile->
      for (entry in Collections.list(jarFile.entries())) {
        var name = entry.name
        if (!name.startsWith(packageName.replace('.', '/'))) continue
        if (!name.endsWith(".class")) continue
        name = name.replace('/', '.').replace(".class$".toRegex(), "")
        loggerComponent.catch(null) {
          val unitClass = Class.forName(name)
          if (AnnotationUtil.hasUnitAnnotation(unitClass))
            identifies.add(UnitIdentify(unitClass, AnnotationUtil.getUnitNameByAnnotation(unitClass)))
        }
      }
    }
    listRegistrar.register(identifies)
  }
}