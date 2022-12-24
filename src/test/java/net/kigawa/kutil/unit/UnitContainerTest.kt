package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.component.config.UnitContainerConfig
import net.kigawa.kutil.unit.component.container.UnitContainer
import net.kigawa.kutil.unit.dummy.*
import net.kigawa.kutil.unit.dummy.parent.*
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import net.kigawa.kutil.unit.extension.registrar.*
import net.kigawa.kutil.unit.util.Assertions
import org.junit.jupiter.api.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class UnitContainerTest: Assertions() {
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
    con.getUnit(InstanceRegistrar::class.java).register(closeable)
    con.removeUnit(closeable.javaClass)
    assertTrue(closed)
  }
  
  @Test
  fun testRegisterSameUnit() {
    val unit = con.getUnit(Unit4::class.java)
    con.getUnit(ClassRegistrar::class.java).register(Unit4::class.java)
    assertSame(unit, con.getUnit(Unit4::class.java))
  }
  
  @Test
  fun testNamedUnit() {
    assertSize(2, con.getUnitList(NamedUnit::class.java))
    assertSize(1, con.getUnitList(NamedUnit::class.java, "a"))
    assertSize(1, con.getUnitList(NamedUnit::class.java, "b"))
    assertSize(0, con.getUnitList(NamedUnit::class.java, "c"))
  }
  
  companion object {
    private val executor = Executors.newCachedThreadPool()
    private val con: UnitContainer = UnitContainer.create()
    
    @JvmStatic
    @BeforeAll
    fun beforeAll() {
      con.getUnit(InstanceRegistrar::class.java).register(executor)
      con.getUnit(UnitContainerConfig::class.java).timeoutSec = 5
      con.getUnit(ResourceRegistrar::class.java).register(UnitContainerTest::class.java)
      con.getUnit(ClassRegistrar::class.java).register(NamedUnit::class.java, "b")
    }
    
    @JvmStatic
    @AfterAll
    fun afterAll() {
      executor.shutdown()
    }
  }
}
