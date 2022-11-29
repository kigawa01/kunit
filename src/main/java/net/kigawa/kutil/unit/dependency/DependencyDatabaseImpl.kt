package net.kigawa.kutil.unit.dependency

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.UnitInfo

class DependencyDatabaseImpl: DependencyDatabase {
  override fun register(unitInfo: UnitInfo, dependencies: MutableList<UnitIdentify>) {
    TODO("Not yet implemented")
  }
  
  override fun contain(unitInfo: UnitIdentify): Boolean {
    TODO("Not yet implemented")
  }
  
  override fun identifyList(): MutableList<UnitIdentify> {
    TODO("Not yet implemented")
  }
  
  override fun findInfo(identify: UnitIdentify): MutableList<UnitInfo> {
    TODO("Not yet implemented")
  }
}