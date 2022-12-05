package net.kigawa.kutil.unit.extension.classlist

import net.kigawa.kutil.unit.exception.RuntimeUnitException

interface ClassList {
  val classes: MutableList<Class<*>>
  val errors: MutableList<Throwable>
  
  companion object {
    @JvmStatic
    fun create(rootClass: Class<*>): ClassList {
      val packageName = rootClass.getPackage()
      val classLoader = rootClass.classLoader
      val resource = classLoader.getResource(packageName.name.replace('.', '/'))
                     ?: throw RuntimeUnitException(rootClass, "could not get resource")
      return when (resource.protocol) {
        JarfileClassList.PROTOCOL->JarfileClassList(resource, packageName)
        FileClassList.PROTOCOL   ->FileClassList(resource, packageName)
        else                     ->throw RuntimeUnitException(rootClass, "could not support file type")
      }
    }
  }
}