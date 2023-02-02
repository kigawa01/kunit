package net.kigawa.kutil.unit.extension

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.*
import net.kigawa.kutil.unitapi.extention.UnitInjector
import net.kigawa.kutil.unitapi.options.FindOptions
import java.util.concurrent.Future

@LateInit
class ContainerInjector(
  private val databaseComponent: UnitDatabaseComponent,
): UnitInjector {
  
  override fun <T: Any> findUnitAsync(identify: UnitIdentify<T>, stack: InitStack,findOptions: FindOptions): List<T> {
    return databaseComponent.findByIdentify(identify).map {it.initOrGet(stack)}.map {it.get()}
  }
}