package net.kigawa.kutil.unitapi.registrar

interface ResourceRegistrar: SelectionRegistrar {
  fun register(rootClass: Class<out Any>) {
    register(rootClass.classLoader, rootClass.`package`.name)
  }
  
  fun register(classLoader: ClassLoader, packageName: String)
}