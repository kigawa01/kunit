package net.kigawa.kutil.unit.extension

import net.kigawa.kutil.unit.annotation.ArgName
import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.api.component.UnitConfigComponent
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.api.component.UnitLoggerComponent
import net.kigawa.kutil.unit.api.extention.UnitReflectionExecutor
import net.kigawa.kutil.unit.exception.UnitException
import java.lang.reflect.Constructor
import java.util.concurrent.TimeUnit

@LateInit
class InjectionReflectionExecutor(
  private val database: UnitDatabaseComponent,
  private val components: UnitConfigComponent,
  private val loggerComponent: UnitLoggerComponent,
): UnitReflectionExecutor {
  override fun <T> callConstructor(constructor: Constructor<T>, stack: InitStack): T? {
    val parameters = constructor.parameters.map {
      val identify = UnitIdentify(it.type, it.getAnnotation(ArgName::class.java)?.name)
      
      val info = database.findOneByEqualsOrClass(identify)
                 ?: throw UnitException("parameter is not found", constructor, identify, stack)
      info.initOrGet(stack.clone())
    }.map {it.get(components.timeoutSec, TimeUnit.SECONDS)}.toTypedArray()
    return loggerComponent.catch(null, constructor, stack) {
      constructor.newInstance(*parameters)
    }
  }
}