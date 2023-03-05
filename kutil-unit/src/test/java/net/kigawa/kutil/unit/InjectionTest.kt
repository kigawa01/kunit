package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.dummy.*
import net.kigawa.kutil.unit.util.AbstractTest
import net.kigawa.kutil.unitapi.registrar.ClassRegistrar
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class InjectionTest: AbstractTest() {
  @Test
  fun testArgNameInjection() {
    val registrar = con.getUnit(ClassRegistrar::class.java)
    assertDoesNotThrow {registrar.register(Unit6::class.java)}
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