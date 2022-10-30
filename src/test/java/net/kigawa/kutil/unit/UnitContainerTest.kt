package net.kigawa.kutil.unit;

import net.kigawa.kutil.unit.container.UnitContainer;
import net.kigawa.kutil.unit.dummy.Unit1;
import net.kigawa.kutil.unit.dummy.Unit2;
import net.kigawa.kutil.unit.dummy.Unit4;
import net.kigawa.kutil.unit.dummy.parent.AbstractUnit;
import net.kigawa.kutil.unit.dummy.parent.AllUnitInterface;
import net.kigawa.kutil.unit.dummy.parent.UnitInterface1;
import net.kigawa.kutil.unit.runtimeexception.RuntimeUnitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class UnitContainerTest extends Assertions
{

    @Test
    void getUnit() {
        var con = new UnitContainer();
        try {
            con.loadUnits(getClass());
        } catch (UnitException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        var executor = Executors.newCachedThreadPool();
        con.loadUnit(executor);

        assertNotNull(con.getUnit(Unit4.class));
        assertNotNull(con.getUnit(Unit1.class));
        assertNotNull(con.getUnit(UnitInterface1.class));
        assertNotNull(con.getUnit(Unit2.class));
        assertNotNull(con.getUnit(AbstractUnit.class));
        assertNotNull(con.getUnit(ExecutorService.class));
        assertThrows(RuntimeUnitException.class, () -> con.getUnit(AllUnitInterface.class));

        executor.shutdown();
    }
}