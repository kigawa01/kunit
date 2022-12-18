package net.kigawa.kutil.unit.extension.executor

import net.kigawa.kutil.unit.component.factory.InitStack
import java.lang.reflect.Executable

interface UnitExecutor {
  fun callExecutable(executable: Executable, stack: InitStack): Any?
}