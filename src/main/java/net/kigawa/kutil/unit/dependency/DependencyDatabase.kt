package net.kigawa.kutil.unit.dependency

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.UnitInfo

interface DependencyDatabase {
  fun register(unitInfo: UnitInfo)
  fun containAmbiguous(identify: UnitIdentify): Boolean {
    return findInfoAmbiguous(identify).isNotEmpty()
  }
  
  fun contain(identify: UnitIdentify): Boolean {
    return findInfo(identify).isNotEmpty()
  }
  
  fun containStrict(identify: UnitIdentify): Boolean {
    return findInfoStrict(identify) != null
  }
  
  fun identifyList(): MutableList<UnitIdentify>
  
  /**
   * find info by class, parents class and name
   * if not found, find by class only
   */
  fun findInfoAmbiguous(identify: UnitIdentify): MutableList<UnitInfo> {
    val list = findInfo(identify)
    if (list.isNotEmpty()) return list
    return findInfo(UnitIdentify(identify.unitClass, null))
  }
  
  /**
   * find info by class, parents class and name
   */
  fun findInfo(identify: UnitIdentify): MutableList<UnitInfo>
  
  /**
   * find info by equal class and name
   */
  fun findInfoStrict(identify: UnitIdentify): UnitInfo?
}