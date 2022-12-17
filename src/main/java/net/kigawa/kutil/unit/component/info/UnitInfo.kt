package net.kigawa.kutil.unit.component.info

import net.kigawa.kutil.unit.extension.identify.UnitIdentify
import net.kigawa.kutil.unit.extension.factory.UnitFactory
import net.kigawa.kutil.unit.extension.getter.UnitGetter

interface UnitInfo<T> {
  val identify: UnitIdentify<T>
  var fail: Boolean
}