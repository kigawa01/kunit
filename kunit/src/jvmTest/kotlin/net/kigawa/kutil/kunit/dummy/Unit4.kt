package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import net.kigawa.kutil.kunit.dummy.parent.UnitInterface1
import net.kigawa.kutil.kunit.dummy.parent.UnitOneToFourInterface
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit
import kotlin.test.Ignore

@Kunit
@Ignore
class Unit4 : AbstractUnit(), UnitInterface1, UnitOneToFourInterface

