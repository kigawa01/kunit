package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import net.kigawa.kutil.kunit.dummy.parent.UnitOneToFourInterface
import kotlin.test.Ignore

@Kunit
@Ignore
object Unit2 : UnitOneToFourInterface {
}