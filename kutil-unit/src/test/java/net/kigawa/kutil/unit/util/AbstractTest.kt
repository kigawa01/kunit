package net.kigawa.kutil.unit.util

import junit.framework.AssertionFailedError
import junit.framework.TestCase
import org.junit.Ignore

@Ignore
open class AbstractTest: TestCase() {
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
}