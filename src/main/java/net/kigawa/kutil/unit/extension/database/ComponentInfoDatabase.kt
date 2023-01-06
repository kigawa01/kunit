package net.kigawa.kutil.unit.extension.database

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.api.component.UnitGetterComponent
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.api.extention.UnitInfoDatabase
import net.kigawa.kutil.unit.extension.getter.InstanceGetter
import net.kigawa.kutil.unit.api.extention.UnitGetter
import net.kigawa.kutil.unit.extension.registeroption.*

@LateInit
class ComponentInfoDatabase(
): UnitInfoDatabase {
  lateinit var getterComponent: UnitGetterComponent
  private val infoList = ConcurrentList<UnitInfo<out Any>>()
  
  init {
    registerComponent(this)
  }
  
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
  
  fun registerComponentClass(itemClass: Class<out Any>) {
    registerComponent(itemClass, getterComponent.findGetter(UnitIdentify(itemClass, null), RegisterOptions()))
  }
  
  fun registerComponent(itemClass: Class<out Any>, getter: UnitGetter) {
    registerComponent(UnitIdentify(itemClass, null), getter)
  }
  
  fun registerComponent(identify: UnitIdentify<out Any>, getter: UnitGetter) {
    val unitInfo = UnitInfo.create(identify, getter)
    unitInfo.initGetter(InitStack())
    infoList.add(unitInfo)
  }
  
  fun unregisterComponent(identify: UnitIdentify<out Any>) {
    infoList.filter {it.identify == identify}.forEach {
      infoList.remove(it)
    }
  }
  
  fun unregisterComponent(clazz: Class<out Any>) {
    unregisterComponent(UnitIdentify(clazz, null))
  }
  
  override fun identifyList(): List<UnitIdentify<out Any>> {
    return infoList.map {
      it.identify
    }
  }
  
  override fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>> {
    @Suppress("UNCHECKED_CAST")
    return infoList.filter {it.identify.equalsOrSuperClass(identify)} as List<UnitInfo<T>>
  }
}