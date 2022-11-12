package net.kigawa.kutil.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnitContainerTest extends Assertions
{

    @Test
    void getUnit()  {
        var con = new UnitContainer();
        try {
            con.loadUnits(getClass());
        } catch (UnitException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        assertNotNull(con.getUnit(UnitDummy.class));
        assertNotNull(con.getUnit(UnitDummy1.class));
        assertNotNull(con.getUnit(UnitDummyInterface.class));
        assertNotNull(con.getUnit(UnitDummy2.class));
        assertThrows(RuntimeUnitException.class, () -> con.getUnit(AllUnitInterface.class));
    }
}