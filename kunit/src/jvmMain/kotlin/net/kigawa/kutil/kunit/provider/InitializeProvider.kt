package net.kigawa.kutil.kunit.provider

import net.kigawa.kutil.kunit.api.annotation.Inject
import net.kigawa.kutil.kunit.dependency.ClassDependency
import net.kigawa.kutil.kunit.exception.NoSingleConstructorException
import net.kigawa.kutil.kunit.executor.Executor
import net.kigawa.kutil.kutil.api.list.containsIf
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class InitializeProvider<T : Any>(
  override val instanceClass: KClass<out T>,
  private val executor: Executor,
) : Provider<T> {
  override fun <U : Any> safeCast(superClass: KClass<U>): Provider<out U>? {
    @Suppress("UNCHECKED_CAST")
    if (superClass.java.isAssignableFrom(instanceClass.java)) return this as Provider<out U>
    return null
  }

  override fun getInstance(): T {
    val constructor = getInitConstructor()
    return executor.execute(constructor)
  }

  override val dependencies: List<ClassDependency<out Any>>
    get() = emptyList()

  private fun getInitConstructor(): KFunction<T> {
    val allFuncs = instanceClass.constructors
    if (allFuncs.size == 1) return allFuncs.first()

    val injectFuncs = allFuncs.filter { func -> func.annotations.containsIf { it is Inject } }
    if (injectFuncs.size == 1) return injectFuncs.first()

    throw NoSingleConstructorException(instanceClass)
  }
}