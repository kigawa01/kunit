package net.kigawa.kutil.unit.component.executor

import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.extension.executor.UnitExecutor
import java.lang.reflect.Constructor

interface UnitExecutorComponent {
  fun addExecutor(executorClass: Class<out UnitExecutor>)
  fun removeExecutor(executorClass: Class<out UnitExecutor>)
  fun <T> callConstructor(constructor: Constructor<T>, initStack: InitStack): T
}