package net.kigawa.kutil.kunit.util

import net.kigawa.kutil.kunit.UnitContainerTest
import net.kigawa.kutil.kunit.api.component.UnitConfigComponent
import net.kigawa.kutil.kunit.api.component.container.UnitContainer
import net.kigawa.kutil.kunit.api.registrar.ClassRegistrar
import net.kigawa.kutil.kunit.api.registrar.InstanceRegistrar
import net.kigawa.kutil.kunit.api.registrar.ResourceRegistrar
import net.kigawa.kutil.kunit.dummy.NamedUnit
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.opentest4j.AssertionFailedError
import java.util.concurrent.Executors
import kotlin.test.Ignore

@Ignore
open class AbstractTest {
  val con = Companion.con
  fun assertContain(expected: Any, actual: Iterable<Any>) {
    if (actual.contains(expected)) return
    throw AssertionFailedError("expected is not contain, expected: $expected, actual: $actual")
  }

  fun assertSize(expected: Int, actual: Collection<Any>) {
    if (expected == actual.size) return
    throw AssertionFailedError("collection size is not match: $expected, actual: $actual")
  }

  fun assertDoesNotThrow(function: () -> Unit) {
    function()
  }

  companion object {
    private val executor = Executors.newCachedThreadPool()
    private val con: UnitContainer = UnitContainer.create()

    @JvmStatic
    @BeforeAll
    fun beforeAll() {
      con.getUnit(InstanceRegistrar::class.java).register(executor)
      con.getUnit(UnitConfigComponent::class.java).timeoutSec = 5
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