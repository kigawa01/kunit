package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.api.component.UnitStoreComponent
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.getter.InstanceStore
import net.kigawa.kutil.unit.extension.registeroption.InstanceOption

interface ComponentDatabase: UnitInfoDatabase {
  var getterComponent: UnitStoreComponent
  override fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean {
    return false
  }
  
  override fun unregister(unitInfo: UnitInfo<out Any>) {
  }
  
  fun registerComponent(item: Any) {
    val instanceGetter = InstanceStore()
    instanceGetter.register(UnitIdentify(item.javaClass, null), RegisterOptions(InstanceOption(item)))
    registerComponent(item.javaClass, instanceGetter)
  }
  
  fun registerComponent(itemClass: Class<out Any>, getter: UnitStore) {
    registerComponent(UnitIdentify(itemClass, null), getter)
  }
  
  fun registerComponentClass(itemClass: Class<out Any>) {
    registerComponent(itemClass, getterComponent.findStore(UnitIdentify(itemClass, null), RegisterOptions()))
  }
  
  fun registerComponent(identify: UnitIdentify<out Any>, getter: UnitStore)
  
  fun unregisterComponent(clazz: Class<out Any>) {
    unregisterComponent(UnitIdentify(clazz, null))
  }
  
  fun unregisterComponent(identify: UnitIdentify<out Any>)
}