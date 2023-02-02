package net.kigawa.kutil.unit.extension

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.UnitInjector
import java.util.concurrent.Future

@LateInit
class ContainerInjector(
  private val databaseComponent: UnitDatabaseComponent,
): UnitInjector {
  
  override fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack): Future<T>? {
    return databaseComponent.findOneByEqualsOrClass(identify)?.initOrGet(stack.clone())
  }
}