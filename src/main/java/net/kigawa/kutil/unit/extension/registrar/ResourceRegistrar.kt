package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.extention.UnitRegistrar
import net.kigawa.kutil.unit.exception.UnitException

@LateInit
class ResourceRegistrar(
  private val jarRegistrar: JarRegistrar,
  private val fileClassRegistrar: FileClassRegistrar,
): UnitRegistrar {
  
  fun register(rootClass: Class<out Any>) {
    val packageName = rootClass.getPackage().name
    val classLoader = rootClass.classLoader
    val resource = classLoader.getResource(packageName.replace('.', '/'))
                   ?: throw UnitException("could not get resource")
    when (resource.protocol) {
      JarRegistrar.PROTOCOL      ->jarRegistrar.register(resource, packageName)
      FileClassRegistrar.PROTOCOL->fileClassRegistrar.register(resource, packageName)
      else                       ->throw UnitException("could not support file type")
    }
  }
}