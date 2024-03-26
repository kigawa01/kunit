package net.kigawa.kutil.kunit

import net.kigawa.kutil.kunit.api.registrar.ClassRegistrar
import net.kigawa.kutil.kunit.dummy.InjectUnit
import net.kigawa.kutil.kunit.dummy.InjectUnitObj
import net.kigawa.kutil.kunit.dummy.Unit6
import net.kigawa.kutil.kunit.util.AbstractTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class InjectionTest : AbstractTest() {
  @Test
  fun testArgNameInjection() {
    val registrar = con.getUnit(ClassRegistrar::class.java)
    assertDoesNotThrow { registrar.register(Unit6::class.java) }
  }

  @Test
  fun injectTest() {
    val fieldInjectUnit = con.getUnit(InjectUnit::class.java)
    assertNotNull(fieldInjectUnit.injectUnit)
    assertNotNull(fieldInjectUnit.finalField)
    assertNotNull(fieldInjectUnit.method)
    assertNotNull(fieldInjectUnit.parentField)
    assertNotNull(fieldInjectUnit.lateInitField)
    assertNotNull(fieldInjectUnit.parentMethod)
    assertNotNull(InjectUnit.staticField)
    assertNotNull(InjectUnit.staticMethod)
  }

  @Test
  fun injectObjTest() {
    val fieldInjectUnit = con.getUnit(InjectUnitObj::class.java)
    assertNotNull(fieldInjectUnit.finalField)
    assertNotNull(fieldInjectUnit.method)
    assertNotNull(fieldInjectUnit.parentField)
    assertNotNull(fieldInjectUnit.lateInitField)
    assertNotNull(fieldInjectUnit.parentMethod)
    assertNotNull(InjectUnitObj.staticField)
    assertNotNull(InjectUnitObj.staticMethod)
  }
}