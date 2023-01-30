package net.kigawa.kutil.unit.extension.database

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitStoreComponent
import net.kigawa.kutil.unit.api.component.UnitInfo
import net.kigawa.kutil.unit.api.extention.*
import net.kigawa.kutil.unit.component.InitStack
import net.kigawa.kutil.unit.component.UnitIdentify
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.registeroption.*

@LateInit
class ComponentDatabaseImpl: ComponentDatabase {
  override lateinit var getterComponent: UnitStoreComponent
  private val infoList = ConcurrentList<UnitInfo<out Any>>()
  
  init {
    registerComponent(this)
  }
  
  override fun registerComponent(identify: UnitIdentify<out Any>, getter: UnitStore) {
    val unitInfo = UnitInfo.create(identify, getter)
    unitInfo.initGetter(InitStack())
    infoList.add(unitInfo)
  }
  
  override fun unregisterComponent(identify: UnitIdentify<out Any>) {
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
    return infoList.filter {it.identify.equalsOrSuperClass(identify)} as List<UnitInfo<T>>
  }
}