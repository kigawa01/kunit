package net.kigawa.kutil.unit.component.database

import net.kigawa.kutil.unit.component.info.UnitInfo
import net.kigawa.kutil.unit.concurrent.ConcurrentList
import net.kigawa.kutil.unit.extension.database.UnitInfoDatabase
import net.kigawa.kutil.unit.extension.identify.UnitIdentify

class UnitInfoDatabaseComponentImpl: UnitInfoDatabaseComponent {
  private val databases = ConcurrentList<UnitInfoDatabase>()
  override fun addDatabase(unitInfoDatabase: UnitInfoDatabase) {
    TODO("Not yet implemented")
  }
  
  override fun removeDatabase(unitInfoDatabase: UnitInfoDatabase) {
    TODO("Not yet implemented")
  }
  
  override fun registerInfo(unitInfo: UnitInfo<*>) {
    TODO("Not yet implemented")
  }
  
  override fun unregisterInfo(unitInfo: UnitInfo<*>) {
    TODO("Not yet implemented")
  }
  
  override fun <T: Any> findOneEquals(identify: UnitIdentify<T>): UnitInfo<T>? {
    TODO("Not yet implemented")
  }
  
  override fun <T: Any> findByClass(unitClass: Class<T>): List<UnitInfo<T>> {
    TODO("Not yet implemented")
  }
}