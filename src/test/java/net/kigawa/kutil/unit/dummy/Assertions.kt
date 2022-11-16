package net.kigawa.kutil.unit.dummy

import org.junit.jupiter.api.AssertionFailureBuilder
import org.junit.jupiter.api.Assertions

open class Assertions : Assertions() {
    fun assertContain(expected: Any, actual: Iterable<Any>) {
        if (actual.contains(expected)) return
        AssertionFailureBuilder.assertionFailure().message("expected is not contain").expected(expected).actual(actual)
            .buildAndThrow()
    }
}