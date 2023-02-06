package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.exception.UnitException
import net.kigawa.kutil.unitapi.extention.UnitRegistrar
import java.net.URL

@LateInit
class ResourceRegistrar(
  private val jarRegistrar: JarRegistrar,
  private val fileClassRegistrar: FileClassRegistrar,
): UnitRegistrar {
  
  fun register(rootClass: Class<out Any>) {
    val packageName = rootClass.getPackage().name
    val classLoader = rootClass.classLoader
    val resources = classLoader.getResources(packageName.replace('.', '/'))
    resources.asIterator().forEach {
      register(packageName, it)
    }
  }
  
  fun register(rootClass: Class<out Any>, packageURL: URL) {
    register(rootClass.getPackage().name, packageURL)
  }
  
  fun register(packageName: String, packageURL: URL) {
    when (packageURL.protocol) {
      JarRegistrar.PROTOCOL      ->jarRegistrar.register(packageURL, packageName)
      FileClassRegistrar.PROTOCOL->fileClassRegistrar.register(packageURL, packageName)
      else                       ->throw UnitException("could not support resource protocol")
    }
  }
}