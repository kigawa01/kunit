package net.kigawa.kutil.unit

import net.kigawa.kutil.unit.container.UnitContainer
import net.kigawa.kutil.unit.dummy.Unit1
import net.kigawa.kutil.unit.dummy.Unit2
import net.kigawa.kutil.unit.dummy.Unit4
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit
import net.kigawa.kutil.unit.dummy.parent.AllUnitInterface
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class UnitContainerTest : Assertions()
{
    @Test
    fun testGetUnit()
    {
        val con = UnitContainer()
        try
        {
            con.loadUnits(javaClass)
        } catch (e: UnitException)
        {
            e.printStackTrace()
            throw RuntimeException(e)
        }
        val executor = Executors.newCachedThreadPool()
        con.loadUnit(executor)

        con.getIdentifies()

        assertNotNull(con.getUnit(Unit4::class.java))
        assertNotNull(con.getUnit(Unit1::class.java))
        assertNotNull(con.getUnit(UnitInterface1::class.java))
        assertNotNull(con.getUnit(Unit2::class.java))
        assertNotNull(con.getUnit(AbstractUnit::class.java))
        assertNotNull(con.getUnit(ExecutorService::class.java))
        assertThrows(RuntimeUnitException::class.java) { con.getUnit(AllUnitInterface::class.java) }
        executor.shutdown()
    }
}