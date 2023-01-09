package net.kigawa.kutil.unit.api.component

import net.kigawa.kutil.unit.annotation.ArgName
import net.kigawa.kutil.unit.api.extention.UnitInjector
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
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