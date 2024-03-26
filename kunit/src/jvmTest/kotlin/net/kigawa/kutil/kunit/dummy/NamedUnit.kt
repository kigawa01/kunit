package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import kotlin.test.Ignore

@Suppress("UNUSED_PARAMETER")
@Kunit(name = "a")
@Ignore
class NamedUnit(unit1: Unit1)