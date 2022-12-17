package net.kigawa.kutil.unit.component.executor

import net.kigawa.kutil.unit.component.factory.InitStack
import java.lang.reflect.Executable

interface ExecutorComponent {
  fun callExecutable(executable: Executable, initStack: InitStack): Any
}