package net.kigawa.kutil.unitapi

import net.kigawa.kutil.unitapi.annotation.ArgName
import java.lang.reflect.Executable

class UnitIdentifies {
  companion object {
    @JvmStatic
    fun createList(executable: Executable): List<UnitIdentify<out Any>> {
      return executable.parameters.map {UnitIdentify(it.type, it.getAnnotation(ArgName::class.java)?.name)}
    }
  }
}