package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.dummy.parent.UnitOneToFourInterface
import kotlin.test.Ignore

@Ignore
@Kunit
class Unit1(private val unitDummy: Unit4, private val unitContainer: UnitContainer) : UnitOneToFourInterface {
}
