package net.kigawa.kutil.unit.factory

import net.kigawa.kutil.unit.UnitIdentify
import net.kigawa.kutil.unit.container.UnitContainer

interface UnitFactory {
  fun isValid(unitIdentify: UnitIdentify): Boolean
  fun init(unitIdentify: UnitIdentify, unitContainer: UnitContainer): Any
  fun dependencies(unitIdentify: UnitIdentify): MutableList<UnitIdentify>
}