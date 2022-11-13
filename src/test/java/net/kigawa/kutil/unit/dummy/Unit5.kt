package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unit.annotation.Dependency
import net.kigawa.kutil.unit.annotation.Unit

@Dependency(value = Unit1::class)
@Dependency(value = Unit2::class)
@Unit
class Unit5 {
}