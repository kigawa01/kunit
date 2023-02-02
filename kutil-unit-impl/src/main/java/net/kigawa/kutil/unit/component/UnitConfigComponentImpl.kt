package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unitapi.annotation.getter.LateInit
import net.kigawa.kutil.unitapi.component.UnitConfigComponent

@LateInit
class UnitConfigComponentImpl: UnitConfigComponent {
  override var timeoutSec: Long = 10
}