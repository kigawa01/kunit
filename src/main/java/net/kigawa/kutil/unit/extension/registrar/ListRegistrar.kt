package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponent
import net.kigawa.kutil.unit.component.factory.InitStack
import net.kigawa.kutil.unit.component.getter.UnitGetterComponent
import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

open class ListRegistrar(
  private val databaseComponent: UnitInfoDatabaseComponent,
  private val getterComponent: UnitGetterComponent,
): UnitRegistrar {
  fun register(identifies: List<UnitIdentify<out Any>>) {
    identifies.map {
      val getter = getterComponent.findGetter(it, mutableListOf())
      val info = UnitInfo.create(it, getter)
      databaseComponent.registerInfo(info)
      Runnable {info.getter.init(it, InitStack())}
    }.forEach {
      it.run()
    }
  }
}