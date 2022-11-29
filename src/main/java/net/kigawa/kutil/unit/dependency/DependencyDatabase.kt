package net.kigawa.kutil.unit.dependency

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.UnitInfo

interface DependencyDatabase {
  fun register(unitInfo: UnitInfo, dependencies: MutableList<UnitIdentify>)
  fun contain(unitInfo: UnitIdentify): Boolean
  fun identifyList(): MutableList<UnitIdentify>
  fun findInfo(identify: UnitIdentify): MutableList<UnitInfo>
}