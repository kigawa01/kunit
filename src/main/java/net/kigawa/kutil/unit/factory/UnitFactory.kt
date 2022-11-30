package net.kigawa.kutil.unit.factory

import net.kigawa.kutil.unit.UnitIdentify

interface UnitFactory {
  fun isValid(unitIdentify: UnitIdentify): Boolean
  fun init(unitIdentify: UnitIdentify, dependencies: List<Any>): Any
  fun dependencies(unitIdentify: UnitIdentify): MutableList<UnitIdentify>
}