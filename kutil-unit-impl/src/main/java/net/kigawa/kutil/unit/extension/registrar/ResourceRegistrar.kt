package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.extention.UnitRegistrar

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