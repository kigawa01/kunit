package net.kigawa.kutil.unit.extension.database

import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.getter.InstanceGetter
import net.kigawa.kutil.unit.extension.getter.UnitGetter
import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.registeroption.*

class ComponentInfoDatabase: UnitInfoDatabase {
  private val infoList = ConcurrentList<UnitInfo<out Any>>()
  override fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean {
    if (registerOptions.contain(DefaultRegisterOption.COMPONENT)) return false
    infoList.add(unitInfo)
    return true
  }
  
  override fun unregister(unitInfo: UnitInfo<out Any>) {
  }
  
  fun registerComponent(item: Any) {
    val instanceGetter = InstanceGetter()
    instanceGetter.register(UnitIdentify(item.javaClass, null), RegisterOptions(InstanceOption(item)))
    registerComponent(item.javaClass, instanceGetter)
  }
  
  fun registerComponent(itemClass: Class<out Any>, getter: UnitGetter) {
    val unitInfo = UnitInfo.create(UnitIdentify(itemClass, null), getter)
    infoList.add(unitInfo)
  }
  
  fun unregisterComponent(identify: UnitIdentify<out Any>) {
    infoList.filter {it.identify == identify}.forEach {
      infoList.remove(it)
    }
  }
  
  override fun identifyList(): List<UnitIdentify<out Any>> {
    return infoList.map {
      it.identify
    }
  }
  
  override fun <T: Any> findByIdentify(identify: UnitIdentify<T>): List<UnitInfo<T>> {
    @Suppress("UNCHECKED_CAST")
    return infoList.filter {it.identify == identify} as List<UnitInfo<T>>
  }
}