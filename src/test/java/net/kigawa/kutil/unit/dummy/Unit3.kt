package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unit.annotation.Unit
import net.kigawa.kutil.unit.dummy.parent.UnitOneToFourInterface
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1

@Unit
class Unit3(unitInterface1: UnitInterface1) :
    UnitOneToFourInterface {
}