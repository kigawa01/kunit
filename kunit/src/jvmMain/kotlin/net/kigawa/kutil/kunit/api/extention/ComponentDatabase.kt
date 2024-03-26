package net.kigawa.kutil.kunit.api.extention

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.options.RegisterOptionEnum
import net.kigawa.kutil.kunit.api.options.RegisterOptions

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