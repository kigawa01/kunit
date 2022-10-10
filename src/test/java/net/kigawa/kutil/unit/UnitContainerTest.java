package net.kigawa.kutil.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UnitContainerTest extends Assertions
{

    @Test
    void getUnit() throws UnitException {
        var con = new UnitContainer(getClass());

        assertNotNull(con.getUnit(UnitDummy.class));
        assertNotNull(con.getUnit(UnitDummy1.class));
        assertNotNull(con.getUnit(UnitDummyInterface.class));
        assertThrows(RuntimeUnitException.class, () -> con.getUnit(AllUnitInterface.class));
    }
}