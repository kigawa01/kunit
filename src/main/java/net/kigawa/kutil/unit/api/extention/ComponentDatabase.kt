package net.kigawa.kutil.unit.api.extention

import net.kigawa.kutil.unit.api.component.UnitGetterComponent
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.extension.getter.InstanceGetter
import net.kigawa.kutil.unit.extension.registeroption.InstanceOption
import net.kigawa.kutil.unit.extension.registeroption.RegisterOptions

interface ComponentDatabase: UnitInfoDatabase {
  var getterComponent: UnitGetterComponent
  override fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean {
    return false
  }
  
  override fun unregister(unitInfo: UnitInfo<out Any>) {
  }
  
  fun registerComponent(item: Any) {
    val instanceGetter = InstanceGetter()
    instanceGetter.register(UnitIdentify(item.javaClass, null), RegisterOptions(InstanceOption(item)))
    registerComponent(item.javaClass, instanceGetter)
  }
  
  fun registerComponent(itemClass: Class<out Any>, getter: UnitGetter) {
    registerComponent(UnitIdentify(itemClass, null), getter)
  }
  
  fun registerComponentClass(itemClass: Class<out Any>) {
    registerComponent(itemClass, getterComponent.findGetter(UnitIdentify(itemClass, null), RegisterOptions()))
  }
  
  fun registerComponent(identify: UnitIdentify<out Any>, getter: UnitGetter)
  
  fun unregisterComponent(clazz: Class<out Any>) {
    unregisterComponent(UnitIdentify(clazz, null))
  }
  
  fun unregisterComponent(identify: UnitIdentify<out Any>)
}