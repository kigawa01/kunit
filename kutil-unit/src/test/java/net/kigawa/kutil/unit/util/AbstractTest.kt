package net.kigawa.kutil.unit.util

import junit.framework.AssertionFailedError
import junit.framework.TestCase
import net.kigawa.kutil.unit.UnitContainerTest
import net.kigawa.kutil.unit.dummy.NamedUnit
import net.kigawa.kutil.unitapi.component.UnitConfigComponent
import net.kigawa.kutil.unitapi.component.UnitContainer
import net.kigawa.kutil.unitapi.registrar.*
import org.junit.*
import java.util.concurrent.Executors

@Ignore
open class AbstractTest: TestCase() {
  val con = AbstractTest.con
  fun assertContain(expected: Any, actual: Iterable<Any>) {
    if (actual.contains(expected)) return
    throw AssertionFailedError("expected is not contain, expected: $expected, actual: $actual")
  }
  
  fun assertSize(expected: Int, actual: Collection<Any>) {
    if (expected == actual.size) return
    throw AssertionFailedError("collection size is not match: $expected, actual: $actual")
  }
  
  fun assertDoesNotThrow(function: ()->Unit) {
    function()
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