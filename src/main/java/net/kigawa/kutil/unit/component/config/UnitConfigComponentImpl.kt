package net.kigawa.kutil.unit.component.config

import net.kigawa.kutil.unit.annotation.LateInit

@LateInit
class UnitConfigComponentImpl: UnitConfigComponent {
  override var timeoutSec: Long = 10
}