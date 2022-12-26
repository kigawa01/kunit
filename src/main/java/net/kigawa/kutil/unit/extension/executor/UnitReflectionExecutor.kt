package net.kigawa.kutil.unit.extension.executor

import net.kigawa.kutil.unit.component.factory.InitStack
import java.lang.reflect.Constructor

interface UnitReflectionExecutor {
  fun <T> callConstructor(constructor: Constructor<T>, stack: InitStack): T?
}