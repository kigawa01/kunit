package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.dummy.Unit1
import net.kigawa.kutil.unit.dummy.Unit2
import net.kigawa.kutil.unit.dummy.Unit4
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit
import net.kigawa.kutil.unit.dummy.parent.AllUnitInterface
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class UnitContainerTest : Assertions() {


    @Test
    fun testGet() {
        assertNotNull(con.getUnit(Unit4::class.java))
        assertNotNull(con.getUnit(Unit1::class.java))
        assertNotNull(con.getUnit(UnitInterface1::class.java))
        assertNotNull(con.getUnit(Unit2::class.java))
        assertNotNull(con.getUnit(AbstractUnit::class.java))
        assertNotNull(con.getUnit(ExecutorService::class.java))
        assertThrows(NoSingleUnitException::class.java) { con.getUnit(AllUnitInterface::class.java) }
    }

    @Test
    fun test() {
        assertFalse(Unit1::class.java == Unit2::class.java)
    }

    companion object {
        private val executor = Executors.newCachedThreadPool()
        private val con: UnitContainer = UnitContainer.create()

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            con.addUnit(executor)
            con.executor = executor::execute
            con.getIdentifies()
            con.registerUnits(ClassList.create(UnitContainerTest::class.java))
            con.initUnits()
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            executor.shutdown()
        }
    }
}
