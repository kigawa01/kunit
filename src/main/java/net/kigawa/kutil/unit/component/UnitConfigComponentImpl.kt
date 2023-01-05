package net.kigawa.kutil.unit.component

import net.kigawa.kutil.unit.annotation.getter.LateInit
import net.kigawa.kutil.unit.api.component.UnitConfigComponent

@LateInit
class UnitConfigComponentImpl: UnitConfigComponent {
  override var timeoutSec: Long = 10
}