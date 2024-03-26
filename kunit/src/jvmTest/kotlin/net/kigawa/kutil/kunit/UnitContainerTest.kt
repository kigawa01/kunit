package net.kigawa.kutil.kunit

import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.exception.NoSingleUnitException
import net.kigawa.kutil.kunit.api.registrar.ClassRegistrar
import net.kigawa.kutil.kunit.api.registrar.InstanceRegistrar
import net.kigawa.kutil.kunit.api.registrar.ResourceRegistrar
import net.kigawa.kutil.kunit.dummy.*
import net.kigawa.kutil.kunit.dummy.parent.UnitInterface1
import net.kigawa.kutil.kunit.dummy.parent.UnitOneToFourInterface
import net.kigawa.kutil.kunit.util.AbstractTest
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit
import org.junit.jupiter.api.Assertions.assertThrows
import java.util.concurrent.ExecutorService
import kotlin.test.*

/**
 * This class contains test methods for the UnitContainer class.
 * It is part of the Unit API component and is used for unit testing.
 */
internal class UnitContainerTest : AbstractTest() {
  @Test
  fun testParent() {
    val container = UnitContainer.create(con)
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
    val con = UnitContainer.create(con)
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
