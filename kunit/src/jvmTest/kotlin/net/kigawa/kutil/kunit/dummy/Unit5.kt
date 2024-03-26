package net.kigawa.kutil.kunit.dummy

import net.kigawa.kutil.kunit.api.annotation.Kunit

@Suppress("unused")
@net.kigawa.kutil.kunit.api.annotation.Dependency(value = Unit1::class)
@net.kigawa.kutil.kunit.api.annotation.Dependency(value = Unit2::class)
@Kunit
class Unit5 {
}