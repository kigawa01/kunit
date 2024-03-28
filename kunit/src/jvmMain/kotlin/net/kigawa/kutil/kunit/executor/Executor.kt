package net.kigawa.kutil.kunit.executor

import net.kigawa.kutil.kunit.context.Context
import net.kigawa.kutil.kunit.provider.Provider
import kotlin.reflect.KCallable
import kotlin.reflect.jvm.jvmErasure

class Executor<R>(
  private val func: KCallable<R>,
  context: Context,
) {
  val dependencies: List<Provider<out Any>> = func.parameters.map {
    context.findProvider(it.type.jvmErasure).first()
  }

  fun execute(): R {
    return dependencies
      .map { it.getInstance() }
      .toTypedArray()
      .let { func.call(*it) }
  }
}