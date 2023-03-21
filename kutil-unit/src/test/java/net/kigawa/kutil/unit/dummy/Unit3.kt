package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unit.dummy.parent.UnitInterface1
import net.kigawa.kutil.unit.dummy.parent.UnitOneToFourInterface
import net.kigawa.kutil.unitapi.annotation.Kunit

@Suppress("UNUSED_PARAMETER")
@Kunit
class Unit3(unitInterface1: UnitInterface1) :
  UnitOneToFourInterface {
}