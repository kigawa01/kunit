package net.kigawa.kutil.unit.extension

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitDatabaseComponent
import net.kigawa.kutil.unit.api.extention.UnitInjector
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import java.util.concurrent.Future

@LateInit
class ContainerInjector(
  private val databaseComponent: UnitDatabaseComponent,
): UnitInjector {
  
  override fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack): Future<T>? {
    return databaseComponent.findOneByEqualsOrClass(identify)?.initOrGet(stack.clone())
  }
}