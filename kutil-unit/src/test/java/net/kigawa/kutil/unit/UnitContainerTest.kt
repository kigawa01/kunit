package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.dummy.*
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1
import net.kigawa.kutil.unit.dummy.parent.UnitOneToFourInterface
import net.kigawa.kutil.unit.util.AbstractTest
import net.kigawa.kutil.unitapi.exception.NoSingleUnitException
import net.kigawa.kutil.unitapi.registrar.ClassRegistrar
import net.kigawa.kutil.unitapi.registrar.InstanceRegistrar
import net.kigawa.kutil.unitapi.registrar.ResourceRegistrar
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.ExecutorService

@RunWith(JUnit4::class)
internal class UnitContainerTest : AbstractTest() {
  @Test
  fun testParent() {
    val container = net.kigawa.kutil.unitapi.component.container.UnitContainer.create(con)
    assertDoesNotThrow { container.getUnit(ResourceRegistrar::class.java) }
    assertDoesNotThrow { container.getUnit(Unit1::class.java) }
    assertSize(2, container.getUnitList(ResourceRegistrar::class.java))
  }

  @Test
  fun testGet() {
    assertNotNull(con.getUnit(Unit4::class.java))
    assertNotNull(con.getUnit(Unit1::class.java))
    assertNotNull(con.getUnit(UnitInterface1::class.java))
    assertNotNull(con.getUnit(Unit2::class.java))
    assertNotNull(con.getUnit(AbstractUnit::class.java))
    assertNotNull(con.getUnit(ExecutorService::class.java))
    assertThrows(NoSingleUnitException::class.java) { con.getUnit(UnitOneToFourInterface::class.java) }
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
    var count = 0
    val closeable = AutoCloseable {
      closed = true
      synchronized(this) {
        count++
      }
    }
    con.getUnit(InstanceRegistrar::class.java).register(closeable)
    con.removeUnit(closeable.javaClass)
    assertTrue(closed)
    assertEquals(1, count)
  }

  @Test
  fun testCloseAbleOnCloseContainer() {
    var count = 0
    val con = net.kigawa.kutil.unitapi.component.container.UnitContainer.create(con)
    var closed = false
    val closeable = AutoCloseable {
      synchronized(this) {
        count++
      }
      closed = true
    }
    con.getUnit(InstanceRegistrar::class.java).register(closeable)
    con.close()
    assertTrue(closed)
    assertEquals(1, count)
  }

  @Test
  fun testRegisterChangeUnit() {
    val unit = con.getUnit(Unit4::class.java)
    con.getUnit(ClassRegistrar::class.java).register(Unit4::class.java)
    assertNotSame(unit, con.getUnit(Unit4::class.java))
  }

  @Test
  fun testNamedUnit() {
    assertSize(2, con.getUnitList(NamedUnit::class.java))
    assertSize(1, con.getUnitList(NamedUnit::class.java, "a"))
    assertSize(1, con.getUnitList(NamedUnit::class.java, "b"))
    assertSize(0, con.getUnitList(NamedUnit::class.java, "c"))
  }

}
