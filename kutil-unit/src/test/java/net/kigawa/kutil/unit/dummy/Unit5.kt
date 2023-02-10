package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unitapi.annotation.Dependency
import net.kigawa.kutil.unitapi.annotation.Kunit

@Suppress("unused")
@Dependency(value = Unit1::class)
@Dependency(value = Unit2::class)
@Kunit
class Unit5 {
}