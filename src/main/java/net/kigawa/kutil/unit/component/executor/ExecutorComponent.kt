package net.kigawa.kutil.unit.component.executor

import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.extension.executor.UnitExecutor
import java.lang.reflect.Executable

interface ExecutorComponent {
  fun addExecutor(executorClass: Class<out UnitExecutor>)
  fun removeExecutor(executorClass: Class<out UnitExecutor>)
  fun callExecutable(executable: Executable, initStack: InitStack): Any
}