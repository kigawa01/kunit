package net.kigawa.kutil.unit.util

object Util {
  fun createLogMessageList(message: String?, item: List<Any?>): List<String> {
    val result = message?.split('\n')?.toMutableList() ?: mutableListOf()
    result.addAll(item.flatMap {
      it ?: return@flatMap mutableListOf<String>()
      String.format("\t %-20s :$it", it.javaClass.simpleName).split('\n')
    })
    return result
  }
  
  fun createLogMessage(message: String?, item: List<Any?>): String {
    return createLogMessageList(message, item).joinToString("\n")
  }
  
  fun instanceOf(clazz: Class<*>, superClass: Class<*>): Boolean {
    if (clazz == superClass) return true
    if (clazz.interfaces.contains(superClass)) return true
    for (interfaceClass in clazz.interfaces) {
      if (instanceOf(interfaceClass, superClass)) return true
    }
    if (clazz.superclass == null) return false
    if (clazz.superclass.equals(superClass)) return true
    if (instanceOf(clazz.superclass, superClass)) return true
    return false
  }
}