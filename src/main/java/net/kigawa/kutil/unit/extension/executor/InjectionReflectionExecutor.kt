package net.kigawa.kutil.unit.extension.executor

import net.kigawa.kutil.unit.annotation.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.component.config.UnitConfigComponent
import net.kigawa.kutil.unit.component.database.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.logger.ContainerLoggerComponent
import net.kigawa.kutil.unit.exception.UnitException
import java.lang.reflect.Constructor
import java.util.concurrent.TimeUnit

@LateInit
class InjectionReflectionExecutor(
  private val database: UnitDatabaseComponent,
  private val components: UnitConfigComponent,
  private val loggerComponent: ContainerLoggerComponent,
): UnitReflectionExecutor {
  override fun <T> callConstructor(constructor: Constructor<T>, stack: InitStack): T? {
    val parameters = constructor.parameters.map {
      val identify = UnitIdentify(it.type, it.name)
      
      val info = database.findOneByEqualsOrClass(identify)
                 ?: throw UnitException("parameter is not found", constructor, identify, stack)
      info.initOrGet(stack.clone())
    }.map {it.get(components.timeoutSec, TimeUnit.SECONDS)}.toTypedArray()
    return loggerComponent.catch(null, constructor, stack) {
      constructor.newInstance(*parameters)
    }
  }
}