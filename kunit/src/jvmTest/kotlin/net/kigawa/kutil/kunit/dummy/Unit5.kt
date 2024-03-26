package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit
import kotlin.test.Ignore

@Suppress("unused")
@net.kigawa.kutil.kunit.api.annotation.Dependency(value = Unit1::class)
@net.kigawa.kutil.kunit.api.annotation.Dependency(value = Unit2::class)
@Kunit
@Ignore
class Unit5 {
}