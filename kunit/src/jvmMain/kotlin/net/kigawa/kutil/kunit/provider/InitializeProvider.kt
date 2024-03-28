package net.kigawa.kutil.kunit.provider

import net.kigawa.kutil.kunit.api.annotation.Inject
import net.kigawa.kutil.kunit.context.Context
import net.kigawa.kutil.kunit.exception.NoSingleConstructorException
import net.kigawa.kutil.kunit.executor.Executor
import net.kigawa.kutil.kutil.api.list.containsIf
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class InitializeProvider<T : Any>(
  override val instanceClass: KClass<out T>,
  context: Context,
) : Provider<T> {
  private val executor = Executor(getInitConstructor(), context)
  override val dependencies: List<Provider<out Any>>
    get() = executor.dependencies


  override fun <U : Any> safeCast(superClass: KClass<U>): Provider<out U>? {
    @Suppress("UNCHECKED_CAST")
    if (superClass.java.isAssignableFrom(instanceClass.java)) return this as Provider<out U>
    return null
  }

  override fun getInstance(): T {
    return executor.execute()
  }


  private fun getInitConstructor(): KFunction<T> {
    val allFuncs = instanceClass.constructors
    if (allFuncs.size == 1) return allFuncs.first()

    val injectFuncs = allFuncs.filter { func -> func.annotations.containsIf { it is Inject } }
    if (injectFuncs.size == 1) return injectFuncs.first()

    throw NoSingleConstructorException(instanceClass)
  }
}