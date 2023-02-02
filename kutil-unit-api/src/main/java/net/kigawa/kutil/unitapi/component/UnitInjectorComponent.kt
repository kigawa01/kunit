package net.kigawa.kutil.unitapi.component

import net.kigawa.kutil.unitapi.annotation.ArgName
import net.kigawa.kutil.unitapi.extention.UnitInjector
import java.lang.reflect.Executable
import java.util.concurrent.Future

interface UnitInjectorComponent: ComponentHolder<UnitInjector> {
  fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack): Future<T>
  fun findUnits(executable: Executable, stack: InitStack): List<Any> {
    return findUnits(
      executable.parameters.map {UnitIdentify(it.type, it.getAnnotation(ArgName::class.java)?.name)},
      stack
    )
  }
  
  fun <T: Any> findUnits(identifies: List<UnitIdentify<out T>>, stack: InitStack): List<T> {
    return identifies.map {
      findUnitAsync(it, stack)
    }.map {it.get()}
  }
}