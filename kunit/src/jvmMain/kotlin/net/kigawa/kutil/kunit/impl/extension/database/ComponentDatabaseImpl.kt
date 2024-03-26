package net.kigawa.kutil.kunit.impl.extension.database

import net.kigawa.kutil.kunit.api.UnitIdentify
import net.kigawa.kutil.kunit.api.annotation.getter.LateInit
import net.kigawa.kutil.kunit.api.component.InitStack
import net.kigawa.kutil.kunit.api.component.UnitInfo
import net.kigawa.kutil.kunit.api.component.UnitStoreComponent
import net.kigawa.kutil.kunit.api.extention.ComponentDatabase
import net.kigawa.kutil.kunit.api.extention.UnitStore
import net.kigawa.kutil.kunit.api.options.RegisterOptions
import net.kigawa.kutil.kunit.api.options.RegistrarInstanceOption
import net.kigawa.kutil.kunit.impl.concurrent.ConcurrentList
import net.kigawa.kutil.kunit.impl.extension.store.InstanceStore

@LateInit
class ComponentDatabaseImpl: ComponentDatabase {
  override lateinit var getterComponent: UnitStoreComponent
  private val infoList = ConcurrentList<UnitInfo<out Any>>()
  
  init {
    registerComponent(this, null)
  }
  
  override fun registerComponent(item: Any, name: String?) {
    val instanceGetter = InstanceStore()
    instanceGetter.register(UnitIdentify(item.javaClass, name), RegisterOptions(RegistrarInstanceOption(item)))
    registerComponent(item.javaClass, instanceGetter)
  }
  
  override fun registerComponent(identify: UnitIdentify<out Any>, getter: UnitStore) {
    val unitInfo = net.kigawa.kutil.kunit.impl.UnitInfoImpl(identify, getter)
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