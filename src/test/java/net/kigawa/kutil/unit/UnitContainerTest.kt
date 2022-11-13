package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.classlist.ClassList
import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.dummy.*
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1
import net.kigawa.kutil.unit.dummy.parent.UnitOneToFourInterface
import net.kigawa.kutil.unit.exception.NoSingleUnitException
import org.junit.jupiter.api.AfterAll
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

    companion object {
        private val executor = Executors.newCachedThreadPool()
        private val con: UnitContainer = UnitContainer.create()

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            con.timeoutSec = 5
            con.addUnit(executor)
            con.executor = executor::execute
            con.getIdentifies()
            con.registerUnits(ClassList.create(UnitContainerTest::class.java)).forEach { throw Exception(it) }
            con.initUnits().forEach { throw Exception(it) }
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            executor.shutdown()
        }
    }
}
