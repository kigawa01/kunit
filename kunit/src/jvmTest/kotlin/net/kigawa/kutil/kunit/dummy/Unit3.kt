package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import net.kigawa.kutil.kunit.dummy.parent.UnitInterface1
import net.kigawa.kutil.kunit.dummy.parent.UnitOneToFourInterface

@Suppress("UNUSED_PARAMETER")
@Kunit
class Unit3(unitInterface1: UnitInterface1) :
  UnitOneToFourInterface {
}