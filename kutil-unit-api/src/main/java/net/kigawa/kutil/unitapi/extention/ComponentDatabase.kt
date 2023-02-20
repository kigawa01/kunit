package net.kigawa.kutil.unitapi.extention

import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.UnitInfo
import net.kigawa.kutil.unitapi.component.UnitStoreComponent
import net.kigawa.kutil.unitapi.options.RegisterOptionEnum
import net.kigawa.kutil.unitapi.options.RegisterOptions

interface ComponentDatabase: UnitInfoDatabase {
  var getterComponent: UnitStoreComponent
  override fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean {
    return false
  }
  
  override fun unregister(unitInfo: UnitInfo<out Any>) {
  }
  
  fun registerComponent(item: Any, name: String?)
  
  fun registerComponent(itemClass: Class<out Any>, getter: UnitStore) {
    registerComponent(UnitIdentify(itemClass, null), getter)
  }
  
  fun registerComponentClass(itemClass: Class<out Any>) {
    registerComponent(
      itemClass, getterComponent.findStore(
        UnitIdentify(itemClass, null),
        RegisterOptions(*RegisterOptionEnum.getOption(itemClass))
      )
    )
  }
  
  fun registerComponent(identify: UnitIdentify<out Any>, getter: UnitStore)
  
  fun unregisterComponent(clazz: Class<out Any>) {
    unregisterComponent(UnitIdentify(clazz, null))
  }
  
  fun unregisterComponent(identify: UnitIdentify<out Any>)
}