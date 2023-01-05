@file:Suppress("unused", "UNUSED_PARAMETER")

package net.kigawa.kutil.unit.dummy

import net.kigawa.kutil.unit.annotation.Inject
import net.kigawa.kutil.unit.annotation.Kunit

@Kunit
class FieldInjectUnit {
  @Inject
  val unit1: Unit2? = null
}