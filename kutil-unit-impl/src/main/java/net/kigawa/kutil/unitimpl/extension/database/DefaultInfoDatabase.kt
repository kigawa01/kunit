package net.kigawa.kutil.unitimpl.extension.database

import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.UnitIdentify
import net.kigawa.kutil.unitapi.component.UnitInfo
import net.kigawa.kutil.unitapi.options.RegisterOptions
import net.kigawa.kutil.unitapi.extention.UnitInfoDatabase

@LateInit
class DefaultInfoDatabase: UnitInfoDatabase {
  private val infoList = ConcurrentList<UnitInfo<out Any>>()
  override fun register(unitInfo: UnitInfo<out Any>, registerOptions: RegisterOptions): Boolean {
    infoList.add(unitInfo)
    return true
  }
  
  override fun unregister(unitInfo: UnitInfo<out Any>) {
    infoList.remove(unitInfo)
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