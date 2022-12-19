package net.kigawa.kutil.unit.extension.registrar

import net.kigawa.kutil.unit.component.database.UnitInfoDatabaseComponent
import net.kigawa.kutil.unit.component.getter.UnitGetterComponent
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class ClassRegistrar(
  private val databaseComponent: UnitInfoDatabaseComponent,
  private val getterComponent: UnitGetterComponent,
): ListRegistrar(databaseComponent, getterComponent) {
  fun register(unitClass: Class<out Any>) {
    register(UnitIdentify(unitClass, null))
  }
  
  fun register(unitClass: Class<out Any>, name: String?) {
    register(UnitIdentify(unitClass, name))
  }
  
  fun register(identify: UnitIdentify<out Any>) {
    register(mutableListOf(identify))
  }
}