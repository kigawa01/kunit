package net.kigawa.kutil.unit.component.info

import net.kigawa.kutil.unit.component.container.UnitIdentify
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.getter.UnitGetter

interface UnitInfo<T> {
  val identify: UnitIdentify<T>
  var getter: UnitGetter
  var factoryClass: Class<out UnitFactory>
  var fail: Boolean
}