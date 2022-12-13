package net.kigawa.kutil.unit.extension.unitconfig

import net.kigawa.kutil.unit.exception.RuntimeUnitException

interface UnitConfigs {
  val classes: MutableList<UnitConfig>
  val errors: MutableList<Throwable>
  
  companion object {
    @JvmStatic
    fun create(rootClass: Class<*>): UnitConfigs {
      val packageName = rootClass.getPackage()
      val classLoader = rootClass.classLoader
      val resource = classLoader.getResource(packageName.name.replace('.', '/'))
                     ?: throw RuntimeUnitException(rootClass, "could not get resource")
      return when (resource.protocol) {
        JarfileClassList.PROTOCOL->JarfileClassList(resource, packageName)
        FileUnitConfigs.PROTOCOL ->FileUnitConfigs(resource, packageName)
        else                     ->throw RuntimeUnitException(rootClass, "could not support file type")
      }
    }
  }
}