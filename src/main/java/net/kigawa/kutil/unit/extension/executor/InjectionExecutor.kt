package net.kigawa.kutil.unit.extension.executor

import net.kigawa.kutil.unit.component.config.UnitConfigComponent
import net.kigawa.kutil.unit.component.database.UnitDatabaseComponent
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.exception.UnitException
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import java.lang.reflect.Constructor
import java.util.concurrent.TimeUnit

class InjectionExecutor(
  private val database: UnitDatabaseComponent,
  private val components: UnitConfigComponent,
): UnitExecutor {
  override fun <T> callConstructor(constructor: Constructor<T>, stack: InitStack): T? {
    val parameters = constructor.parameters.map {
      val identify = UnitIdentify(it.type, it.name)
      database.findOneByEqualsOrClass(identify)?.getter?.initOrGet(identify, stack.clone())
      ?: throw UnitException("parameter is not found")
    }.map {it.get(components.timeoutSec, TimeUnit.SECONDS)}.toTypedArray()
    return constructor.newInstance(*parameters)
  }
}