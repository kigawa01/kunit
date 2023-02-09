package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.dummy.*
import net.kigawa.kutil.unit.dummy.parent.*
import net.kigawa.kutil.unit.registrar.ClassRegistrarImpl
import net.kigawa.kutil.unit.registrar.InstanceRegistrarImpl
import net.kigawa.kutil.unit.util.AbstractTest
import net.kigawa.kutil.unitapi.component.UnitConfigComponent
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.exception.NoSingleUnitException
import net.kigawa.kutil.unitapi.registrar.*
import org.junit.*
import org.junit.Assert.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RunWith(JUnit4::class)
internal class UnitContainerTest: AbstractTest() {
  @Test
  fun testParent() {
    val container = UnitContainer.create(con)
    assertSize(1, container.getUnitList(ResourceRegistrar::class.java))
  }
  
  @Test
  fun testGet() {
    assertNotNull(con.getUnit(Unit4::class.java))
    assertNotNull(con.getUnit(Unit1::class.java))
    assertNotNull(con.getUnit(UnitInterface1::class.java))
    assertNotNull(con.getUnit(Unit2::class.java))
    assertNotNull(con.getUnit(AbstractUnit::class.java))
    assertNotNull(con.getUnit(ExecutorService::class.java))
    assertThrows(NoSingleUnitException::class.java) {con.getUnit(UnitOneToFourInterface::class.java)}
  }
  
  @Test
  fun testGetUnits() {
    val list = con.getUnitList(UnitOneToFourInterface::class.java)
    assertContain(con.getUnit(Unit1::class.java), list)
    assertContain(con.getUnit(Unit2::class.java), list)
    assertContain(con.getUnit(Unit3::class.java), list)
    assertContain(con.getUnit(Unit4::class.java), list)
  }
  
  @Test
  fun testCloseAble() {
    var closed = false
    val closeable = AutoCloseable {
      closed = true
    }
    con.getUnit(InstanceRegistrarImpl::class.java).register(closeable)
    con.removeUnit(closeable.javaClass)
    assertTrue(closed)
  }
  
  @Test
  fun testRegisterChangeUnit() {
    val unit = con.getUnit(Unit4::class.java)
    con.getUnit(ClassRegistrarImpl::class.java).register(Unit4::class.java)
    assertNotSame(unit, con.getUnit(Unit4::class.java))
  }
  
  @Test
  fun testNamedUnit() {
    assertSize(2, con.getUnitList(NamedUnit::class.java))
    assertSize(1, con.getUnitList(NamedUnit::class.java, "a"))
    assertSize(1, con.getUnitList(NamedUnit::class.java, "b"))
    assertSize(0, con.getUnitList(NamedUnit::class.java, "c"))
  }
  
  @Test
  fun testArgNameInjection() {
    val registrar = con.getUnit(ClassRegistrarImpl::class.java)
    assertDoesNotThrow {registrar.register(Unit6::class.java)}
  }
  
  @Test
  fun fieldInject() {
    val fieldInjectUnit = con.getUnit(FieldInjectUnit::class.java)
    assertNotNull(fieldInjectUnit.unit1)
    assertNotNull(fieldInjectUnit.unit3)
    assertNull(FieldInjectUnit.unit2)
  }
  
  @Test
  fun methodInject() {
    val fieldInjectUnit = con.getUnit(MethodInjectUnit::class.java)
    assertNotNull(fieldInjectUnit.unit1)
    assertNotNull(fieldInjectUnit.unit3)
    assertNull(FieldInjectUnit.unit2)
  }
  
  companion object {
    private val executor = Executors.newCachedThreadPool()
    private val con: UnitContainer = UnitContainer.create()
    
    @JvmStatic
    @BeforeClass
    fun beforeAll() {
      con.getUnit(InstanceRegistrar::class.java).register(executor)
      con.getUnit(UnitConfigComponent::class.java).timeoutSec = 5
      con.getUnit(ResourceRegistrar::class.java).register(UnitContainerTest::class.java)
      con.getUnit(ClassRegistrar::class.java).register(NamedUnit::class.java, "b")
    }
    
    @JvmStatic
    @AfterClass
    fun afterAll() {
      executor.shutdown()
    }
  }
}
