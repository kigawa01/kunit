package net.kigawa.kutil.unit.extension.identify

import net.kigawa.kutil.unit.exception.RuntimeUnitException

interface UnitIdentifies {
  val classes: MutableList<UnitIdentify<*>>
  val errors: MutableList<Throwable>
  
  companion object {
    @JvmStatic
    fun create(rootClass: Class<*>): UnitIdentifies {
      val packageName = rootClass.getPackage()
      val classLoader = rootClass.classLoader
      val resource = classLoader.getResource(packageName.name.replace('.', '/'))
                     ?: throw RuntimeUnitException(rootClass, "could not get resource")
      return when (resource.protocol) {
        JarUnitIdentifies.PROTOCOL ->JarUnitIdentifies(resource, packageName)
        FileUnitIdentifies.PROTOCOL->FileUnitIdentifies(resource, packageName)
        else                       ->throw RuntimeUnitException(rootClass, "could not support file type")
      }
    }
  }
}