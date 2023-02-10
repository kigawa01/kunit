package net.kigawa.kutil.unitapi.registrar

interface ResourceRegistrar {
  fun register(rootClass: Class<out Any>) {
    register(rootClass.classLoader, rootClass.`package`)
  }
  
  fun register(classLoader: ClassLoader, loadPackage: Package)
}